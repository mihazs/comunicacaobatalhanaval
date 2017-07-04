/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages.request;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import messages.Source;

/**
 *
 * @author Mihael Zamin
 */

public class Request {
    private Source source;
    private String resourcePath;
    private Map<String, Object> content = new HashMap<>();

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    
    @Override
    public String toString() {
        return "Request{" + "resourcePath=" + resourcePath + ", content=" + content + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Request other = (Request) obj;
        if (!Objects.equals(this.resourcePath, other.resourcePath)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }
    
    
    
}
