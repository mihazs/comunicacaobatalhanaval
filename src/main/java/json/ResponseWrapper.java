/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.response.Response;



/**
 *
 * @author Mihael Zamin
 */
public class ResponseWrapper {
    private Response responseObject;
   
    private boolean broadcast;
    private ObjectMapper objectMapper;
    
    public ResponseWrapper() {
    }

    public ResponseWrapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    

    public Response getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Response responseObject) {
        this.responseObject = responseObject;
    }

    
   

    public ResponseWrapper(Response responseObject, boolean broadcast, ObjectMapper objectMapper) {
        this.responseObject = responseObject;
        this.broadcast = broadcast;
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    

  
    
    
    

    
    public String getResponse() {
        try {
            return objectMapper.writeValueAsString(this.responseObject);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ResponseWrapper.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

   

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
    
}
