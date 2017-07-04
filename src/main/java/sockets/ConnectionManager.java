/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import json.MessageProcessor;
import messages.Source;
import messages.request.Request;
import messages.response.Response;

/**
 *
 * @author Mihael Zamin
 */
public abstract class ConnectionManager {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private MessageProcessor messageProcessor = new MessageProcessor();

    protected ObjectOutputStream getOutput() {
        return output;
    }

    protected ObjectInputStream getInput() {
        return input;
    }

    protected MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }
    public ConnectionManager(Socket socket, MessageProcessor messageProcessor) throws IOException{
        this(socket);
        this.messageProcessor = messageProcessor;
        
    }
    public ConnectionManager(Socket socket) throws IOException{
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Estabelecendo nova conexão por socket");
       this.socket = socket; 
       output = new ObjectOutputStream(socket.getOutputStream());
       input = new ObjectInputStream(socket.getInputStream());
    }
    public Socket getSocket() {
        return socket;
    }
    
}
