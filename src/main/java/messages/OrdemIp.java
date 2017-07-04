/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mihael Zamin
 */
public class OrdemIp {
    private List<Integer> ordem = new ArrayList();
    private Map<Integer, String> ip = new HashMap();

    public List<Integer> getOrdem() {
        return ordem;
    }

    public void setOrdem(List<Integer> ordem) {
        this.ordem = ordem;
    }

    public Map<Integer, String> getIp() {
        return ip;
    }

    public void setIp(Map<Integer, String> ip) {
        this.ip = ip;
    }
    
    
    
}
