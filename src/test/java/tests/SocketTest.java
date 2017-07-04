/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import json.MessageProcessor;
import json.ResponseWrapper;
import junit.framework.TestCase;
import messages.Source;
import messages.request.Request;
import messages.response.Response;
import org.junit.Ignore;
import org.junit.Test;
import sockets.client.Client;
import sockets.server.Server;

/**
 *
 * @author Mihael Zamin
 */
public class SocketTest extends TestCase {
    /*
    @Test
    public void testServidor() throws IOException{
        Server s = new Server(9987, "resources");
        s.run();
    }
    @Test
    public void testClient() throws IOException, ClassNotFoundException{
        String retornoEsperado = "Hello Mihael1";
        Source s = new Source();
        s.setId(1);
        Client c = new Client(s, "127.0.0.1", 9987);
        Request r = new Request();
        r.setResourcePath("helloworld/helloparam");
        Map<String, Object> m = new HashMap();
        m.put("nome", "Mihael");
        m.put("matriz", new int[5][5]);
        r.setContent(m);
        c.sendRequest(r);
        Response[] rs = c.getResponses();
        String retornoObtido = String.valueOf(rs[0].getContent());
        assertEquals(retornoEsperado, retornoObtido);
    }*/
    
    @Test
    public void testClientAndServidor() throws IOException, ClassNotFoundException{
        String retornoEsperado = "Hello Mihael1";
        String retornoObtido = "";
        //instanciação do servidor
        Server server = new Server(9987, "resources");
        Thread t = new Thread(server);
        t.start();
                
        //
        Source source = new Source();
        source.setId(1);
        Client c = new Client(source, "127.0.0.1", 9987);
        Request r = new Request();
        r.setResourcePath("helloworld/helloparam");
        Map<String, Object> m = new HashMap();
        m.put("nome", "Mihael");
        r.setContent(m);
        c.sendRequest(r);
        Response[] rs = c.getResponses();
        retornoObtido = String.valueOf(rs[0].getContent());
        assertEquals(retornoEsperado, retornoObtido);
        
        
        //parando servidor
        server.stopServer();
    }
    
}
