package reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.Status;
import messages.Source;
import messages.request.Request;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.reflections.Reflections;
import reflect.annotations.ParamName;
import reflect.annotations.Path;
import reflect.annotations.ServerInfo;
import reflect.annotations.UserInfo;
import reflect.annotations.casting.Broadcast;
import reflect.exceptions.ProtocolException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mihael Zamin
 */
public class ResourceManager {

    private Set<Class<?>> resources;
    private Map<String, Class<?>> resourceMap;
    private Map<String, Map<String, Method>> resourceMethodMap;
    

    public ResourceManager(){
    this("resources");
    }
    public ResourceManager(String resourcePackage) {
        resources = new Reflections(resourcePackage).getTypesAnnotatedWith(reflect.annotations.Resource.class);
        resourceMap = Collections.synchronizedMap(new CaseInsensitiveMap());
        resourceMethodMap = Collections.synchronizedMap(new CaseInsensitiveMap());
        Map<String, Method> rme = new HashMap<>();
        for (Class c : resources) {
            if (c.isAnnotationPresent(reflect.annotations.Path.class)) {

                Path pat = (Path) c.getAnnotation(reflect.annotations.Path.class);
                if (pat != null) {
                    resourceMap.put(pat.value(), c);
                    Method[] ms = c.getMethods();
                    for (Method meth : ms) {
                        Path caminho = meth.getDeclaredAnnotation(reflect.annotations.Path.class);
                        if (caminho != null) {
                            rme.put(caminho.value(), meth);
                        }
                    }
                    resourceMethodMap.put(pat.value(), rme);
                }
            }

        }
    }

    public Class getClassByUri(String path) throws ProtocolException {
        String[] paths = splitPath(path);
        if (paths != null) {

            Class c = resourceMap.get(paths[0]);
            if(c != null)
                return c;

        }
        throw new ProtocolException(Status.NOT_FOUND, "Não há recurso para o caminho solicitado.");
    }
     public Object invokeResource(Request r, ServerInformation serverInfo) throws ProtocolException{
         if(r.getResourcePath().isEmpty())
            throw new ProtocolException(Status.BAD_REQUEST, "Caminho da requisição não especificado");
        return invokeResource(r.getResourcePath(), r.getContent(), r.getSource(), serverInfo);
     }

    public Object invokeResource(Request r) throws ProtocolException {
        return invokeResource(r, null);
    }

    public Object invokeResource(String path, Map<String, Object> args, Source source) throws ProtocolException{
        return invokeResource(path, args, source, null);
    }

    public Object invokeResource(String path, Map<String, Object> args, Source source, ServerInformation serverInfo) throws ProtocolException {
        Method m;
        try {
            m = getMethodByUri(path);
        } catch (Exception ex) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new ProtocolException(Status.NOT_FOUND, "Não há recurso para o caminho solicitado.");
        }
        args = new CaseInsensitiveMap(args);
        if (m == null) {
             throw new ProtocolException(Status.NOT_FOUND, "Não há recurso para o caminho solicitado.");
            
        } else {
            Parameter[] p = m.getParameters();
            if (p.length == args.size()) {
                Object[] a = new Object[p.length];
                for (int i = 0; i < p.length; i++) {
                    ParamName pn = (ParamName) p[i].getAnnotation(ParamName.class);
                    if (pn != null) {
                        String n = pn.value();
                        if (args.containsKey(n)) {
                            a[i] = p[i].getType().cast(args.get(n));
                        } else{
                            a[i] = null;
                        }
                    }
                   
                }
                
                try {
                    Class c = m.getDeclaringClass();
                    Object o = c.newInstance();
                    if(source != null){
                    Field[] fields = c.getFields();
                    for(Field f: fields){
                        if(f.getAnnotation(UserInfo.class) != null){
                            f.setAccessible(true);
                            f.set(o, source);
                        }else if(f.getAnnotation(ServerInfo.class) != null){
                            if (serverInfo != null) {
                                f.setAccessible(true);
                                f.set(o, serverInfo);
                            }
                        }
                    }
                    }
                    return m.invoke(o, a);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                    throw new ProtocolException(Status.INTERNAL_SERVER_ERROR);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                     throw new ProtocolException(Status.BAD_REQUEST, "Os argumentos enviados não são compatíveis com os argumentos declarados no recurso");
                    
                } 
            } else {
                throw new ProtocolException(Status.BAD_REQUEST, "A quantidade de argumentos passados não condiz com a quantidade de argumentos exigidos pelo método.\nEsperado: " + p.length + " obtido: " + args.size());
            }
            
        }
    }
    public boolean isBroadcast(Request r) throws ProtocolException{
        if(!r.getResourcePath().isEmpty())
            return isBroadcast(r.getResourcePath());
        else throw new ProtocolException(Status.BAD_REQUEST, "Caminho do recurso vazio");
    }
    public boolean isBroadcast(String path) throws ProtocolException{
        Method m = getMethodByUri(path);
       return (m.getAnnotation(Broadcast.class) != null);
    }
    private String[] splitPath(String path) {
        String[] d = path.split("/");
        String[] paths = null;
        for (int i = 0; i < d.length; i++) {
            if (!d[i].isEmpty()) {
                paths = new String[2];
                paths[0] = d[i];
                paths[1] = d[i + 1];
                break;
            }
        }
        return paths;
    }

    public Method getMethodByUri(String path) throws ProtocolException {
        String[] paths = splitPath(path);
        if (paths != null) {
            if (resourceMethodMap.containsKey(paths[0])) {
                Map<String, Method> rme = resourceMethodMap.get(paths[0]);
                if (rme.containsKey(paths[1])) {
                    return rme.get(paths[1]);
                }
            }
        }
        throw new ProtocolException(Status.NOT_FOUND, "Não há recurso para o caminho solicitado.");
    }

}
