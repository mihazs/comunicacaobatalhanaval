/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflect.exceptions;

import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Mihael Zamin
 */
public class ProtocolException extends Exception{
    private Status status;
    
    public ProtocolException(Status status) {
        super();
        this.status = status;
    }
    public ProtocolException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
