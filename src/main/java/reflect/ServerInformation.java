/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mihael Zamin
 */
public class ServerInformation {
    private Map<Integer, String> hostsConnected;

    public ServerInformation() {
        hostsConnected = new HashMap();
    }

    public Map<Integer, String> getHostsConnected() {
        return hostsConnected;
    }

    public void setHostsConnected(Map<Integer, String> hostsConnected) {
        this.hostsConnected = hostsConnected;
    }
    
    
    
}
