/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.server;

import com.fasterxml.uuid.Generators;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import sockets.ConnectionManager;
import json.ResponseWrapper;
import messages.response.Response;

/**
 *
 * @author Mihael Zamin
 */
public class RequestHandler extends ConnectionManager implements Runnable {
    
    private final Server server;
    private Queue<ResponseWrapper> send;
    private boolean stop;
    private final UUID uid;
    public boolean isStopped() {
        return stop;
    }

    public void stopHandling() {
        stop = true;
    }
    
    public RequestHandler(Socket socket, Server server, int id) throws IOException {
        super(socket, server.getMessageProcessor());
        this.server = server;
        send = new LinkedList<>();
        uid = Generators.randomBasedGenerator().generate();
        this.server.getServerInformation().getHostsConnected().put(id, socket.getInetAddress().toString());
       
        
    }

    public void acceptRequest() throws IOException, ClassNotFoundException{
        String str = String.valueOf(this.getInput().readObject());
        ResponseWrapper rw = getMessageProcessor().processRequest(str);
        if(rw.isBroadcast()){
            server.broadcast(rw, this);
        }
        send.add(rw);
    }
    public void sendResponse(ResponseWrapper rw) throws IOException{
        send.add(rw);
    }
    @Override
    public void run() {
        Response r;
        ResponseWrapper rw;
        while(!stop){
            try {
                acceptRequest();
                while(!send.isEmpty()){
                    rw = send.poll();
                    rw.getResponseObject().setInQueue(send.size());
                    getOutput().writeObject(rw.getResponse());
                }
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                stop = true;
            } catch (ClassNotFoundException ex) {
                stop = true;
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            this.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.uid);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RequestHandler other = (RequestHandler) obj;
        if (!Objects.equals(this.uid, other.uid)) {
            return false;
        }
        return true;
    }
    
    
}
