/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import messages.Source;
import reflect.annotations.ParamName;
import reflect.annotations.Path;
import reflect.annotations.Resource;
import reflect.annotations.UserInfo;


/**
 *
 * @author Mihael Zamin
 */
@Resource
@Path("helloworld")
public class HelloWorldResource {
    @UserInfo
    public Source source;
    
    @Path("hello")
    public String hello(){
        return "Hello mihael";
    }
    @Path("helloparam")
    public String helloparam(@ParamName("nome") String nome){
        
        return "Hello " + nome + source.getId();
    }
    @Path("sourceid")
    public int getSourceId(){
        return this.source.getId();
    }
}
