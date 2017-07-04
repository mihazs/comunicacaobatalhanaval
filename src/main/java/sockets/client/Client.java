/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import json.MessageProcessor;
import messages.Source;
import messages.request.Request;
import messages.response.Response;
import sockets.ConnectionManager;

/**
 *
 * @author Mihael Zamin
 */
public class Client extends ConnectionManager{
    private Source source;
    
    
    public Client(Source source, String host, int port) throws UnknownHostException, IOException{
        super(new Socket(InetAddress.getByName(host), port));
        this.getSocket().setSoTimeout(10000);
        this.source = source;
    }
    
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
    public Response[] getResponses() throws IOException, ClassNotFoundException{
        Response r; 
        List<Response> respostas = new ArrayList();
        do{
        r = getMessageProcessor().processResponse(String.valueOf( getInput().readObject()));
        respostas.add(r);
        this.setSource(r.getRequest().getSource());
        }while(r.getInQueue() > 0);
        return respostas.toArray(new Response[0]);
    }
    public void sendRequest(Request request) throws IOException, ClassNotFoundException{
        request.setSource(this.getSource());
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Enviando requisi\u00e7\u00e3o: {0}", request.toString());
        getOutput().writeObject(getMessageProcessor().prepareRequest(request));
        
    }
    public static List<String> getInterfaceIps() throws SocketException{
        List<String> retorno = new ArrayList<>();
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                retorno.add(i.getHostAddress());
            }
        }
        return retorno;
    }
   
    
}
