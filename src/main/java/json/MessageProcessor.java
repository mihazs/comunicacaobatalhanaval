/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.Status;
import messages.request.Request;
import messages.response.Response;
import reflect.ResourceManager;
import reflect.ServerInformation;
import reflect.exceptions.ProtocolException;

/**
 *
 * @author Mihael Zamin
 */
public class MessageProcessor {
    private ObjectMapper om = new ObjectMapper().enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    private ResourceManager rm;
    private ServerInformation serverInformation;

    
    
    public MessageProcessor(String resourcePackage, ServerInformation serverInformation) {
        this(resourcePackage);
        this.serverInformation = serverInformation;
    }

    public MessageProcessor(ServerInformation serverInformation) {
        this();
        this.serverInformation = serverInformation;
    }
    
    public MessageProcessor(){
        this("");
    }
    public MessageProcessor(String resourcePackage){
        if(resourcePackage.isEmpty())
            resourcePackage = "resources";
        rm = new ResourceManager(resourcePackage);
    }
    public Response processResponse(String s){
        Response r = null;
        try {
           r = om.readValue(s, Response.class);
        } catch (IOException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return r;
    }
    
    public ResponseWrapper processRequest(Request r){
        ResponseWrapper rw = new ResponseWrapper(om);
         try {
            Object o = rm.invokeResource(r, serverInformation);
            rw.setResponseObject(new Response(r,o, Status.OK));
            
            rw.setBroadcast(rm.isBroadcast(r));
            rw.getResponseObject().setBroadcast(rw.isBroadcast());
            
        } catch (ProtocolException ex1) {
            rw.setResponseObject(new Response(ex1.getMessage(), ex1.getStatus()));
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex1);
        }
        
        return rw;
    }
    public String prepareRequest(Request r){
        try {
            return om.writeValueAsString(r);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public ResponseWrapper processRequest(String sr){
        
        try {
            Request r = null;
            r = om.readValue(sr, Request.class);
            return processRequest(r);
        } catch (IOException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            ResponseWrapper rw = new ResponseWrapper();
            rw.setResponseObject(new Response("Erro ao processar requisição", Status.INTERNAL_SERVER_ERROR));
            
            return rw;
        }
        
        
    }

    public ServerInformation getServerInformation() {
        return serverInformation;
    }

    public void setServerInformation(ServerInformation serverInformation) {
        this.serverInformation = serverInformation;
    }
}
