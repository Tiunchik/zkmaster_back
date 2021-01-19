package org.zkmaster.backend.entity.dto;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class TransformDTO {
    private String nodePath;
    private String type;
    private List<String> content;
    
    public TransformDTO() {
    }
    
    public TransformDTO(String nodePath, String type, List<String> content) {
        this.nodePath = nodePath;
        this.type = type;
        
        this.content = content;
    }
    
    public String getNodePath() {
        return nodePath;
    }
    
    public String getType() {
        return type;
    }
    
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<String> getContent() {
        return content;
    }
    
    public void setContent(List<String> content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        TransformDTO that = (TransformDTO) object;
        return Objects.equals(nodePath, that.nodePath)
                && Objects.equals(type, that.type)
                && Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nodePath, type, content);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", TransformDTO.class.getSimpleName() + "[", "]")
                .add("nodePath='" + nodePath + "'")
                .add("transformType='" + type + "'")
                .add("content=" + content)
                .toString();
    }
    
}
