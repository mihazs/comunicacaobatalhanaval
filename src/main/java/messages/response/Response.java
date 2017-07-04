/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.response;

import java.util.Objects;
import messages.request.Request;
import javax.ws.rs.core.Response.Status;
/**
 *
 * @author Mihael Zamin
 */
public class Response {
    private Request request;
    private Object content;
    private Status status;
    private int inQueue;
    private boolean broadcast;
    
    public Response() {
    }

    public Response(Status status) {
        this.status = status;
    }

    public Response(Object content, Status status) {
        this.content = content;
        this.status = status;
    }

    
    public Response(Request request, Object content, Status status) {
        this.request = request;
        this.content = content;
        this.status = status;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
    
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getInQueue() {
        return inQueue;
    }

    public void setInQueue(int inQueue) {
        this.inQueue = inQueue;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Response other = (Response) obj;
        if (!Objects.equals(this.request, other.request)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        return true;
    }
    
    
}
