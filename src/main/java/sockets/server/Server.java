/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import json.MessageProcessor;
import json.ResponseWrapper;
import messages.response.Response;
import reflect.ServerInformation;

/**
 *
 * @author Mihael Zamin
 */
public class Server implements Runnable {
    private MessageProcessor messageProcessor;
    private ServerSocket serversocket;
    private List<RequestHandler> connections;
    private boolean stop;
    private ServerInformation serverInformation = new ServerInformation();

    public List<RequestHandler> getConnections() {
        return connections;
    }

    public void setConnections(List<RequestHandler> connections) {
        this.connections = connections;
    }

    public boolean isStopped() {
        return stop;
    }

    public void stopServer() {
        stop = true;
    }

    public ServerInformation getServerInformation() {
        return serverInformation;
    }

    public void setServerInformation(ServerInformation serverInformation) {
        this.serverInformation = serverInformation;
    }
    
    

    public ServerSocket getServersocket() {
        return serversocket;
    }
    public Server(int port) throws IOException{
        this(port, "resources");
    }
    public Server(int port, String resourcePackage) throws IOException{
        serversocket = new ServerSocket(port);
        connections = new ArrayList<>();
        stop = false;
        messageProcessor = new MessageProcessor(resourcePackage);
        
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }
    public boolean broadcast(ResponseWrapper r){
            r.setBroadcast(false);
            for(RequestHandler rh : connections){
                try {
                    rh.sendResponse(r);

                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
            return true;
        }

    public boolean broadcast(ResponseWrapper r, RequestHandler from){
        
        for(RequestHandler rh : connections){
            try {
                if(!rh.equals(from)){
                rh.sendResponse(r);
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    /*
    public ReponseWrapper acceptRequest() throws IOException, ClassNotFoundException{
        String str = String.valueOf(this.getInput().readObject());
        return mp.processRequest(str);
    }*/
    @Override
    public void run(){
        RequestHandler rh;
       
        while(!stop){
            try {
                rh = new RequestHandler(serversocket.accept(), this, connections.size());
                connections.add(rh);
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Conex\u00e3o estabelecida com: {0}", rh.getSocket().getInetAddress().toString());
                new Thread(rh).start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
}
