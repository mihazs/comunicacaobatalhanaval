/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import json.MessageProcessor;
import json.ResponseWrapper;
import junit.framework.TestCase;
import messages.Source;
import messages.request.Request;
import messages.response.Response;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Mihael Zamin
 */

public class JsonAndReflectionTest extends TestCase{

    @Test
    public void testNames() throws JsonProcessingException, IOException {
        Request r = new Request();
        Request r2 = new Request();
        r.setResourcePath("/helloworld/hello");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(r);
        System.out.println(s);
        r2 = om.readValue(s, Request.class);
        assertEquals(r, r2);

    }
    
    @Test
    public void testSourceInjection() throws JsonProcessingException, IOException {
        int esperado = 1;
        int obtido = 0;
        Request r = new Request();
        r.setResourcePath("/helloworld/sourceid");
        Source src = new Source();
        src.setId(esperado);
        r.setSource(src);
        MessageProcessor mp = new MessageProcessor("resources");
        ResponseWrapper rw = mp.processRequest(r);
        obtido = Integer.parseInt(String.valueOf(rw.getResponseObject().getContent()));
        System.out.println("Retorno obtido da injeção: " + obtido);
        assertEquals(esperado, obtido);
        

    }
    @Test
    public void testResponseClass() throws JsonProcessingException, IOException{
        Response r = new Response();
        Response r2 = new Response();
        r.setStatus(javax.ws.rs.core.Response.Status.OK);
        ObjectMapper om = new ObjectMapper().enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        String s = om.writeValueAsString(r);
        r2 = om.readValue(s, Response.class);
        System.out.println(r2.toString());
        assertEquals(r, r2);
        
    }
   
    
}
