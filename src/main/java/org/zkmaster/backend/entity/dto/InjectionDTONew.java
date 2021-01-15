package org.zkmaster.backend.entity.dto;

import org.zkmaster.backend.controllers.APIController;
import org.zkmaster.backend.entity.ZKNode;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Use specially for injection node from Source real-server to Other real-server.
 *
 * @see APIController
 */
public class InjectionDTONew {
    private List<ZKNode> createNodeList;
    private List<ZKNode> updateNodeList;
    private String targetHost;
    
    public InjectionDTONew() {
    }
    
    public InjectionDTONew(List<ZKNode> createNodeList, List<ZKNode> updateNodeList, String targetHost) {
        this.createNodeList = createNodeList;
        this.updateNodeList = updateNodeList;
        this.targetHost = targetHost;
    }
    
    public List<ZKNode> getCreateNodeList() {
        return createNodeList;
    }
    
    public void setCreateNodeList(List<ZKNode> createNodeList) {
        this.createNodeList = createNodeList;
    }
    
    public List<ZKNode> getUpdateNodeList() {
        return updateNodeList;
    }
    
    public void setUpdateNodeList(List<ZKNode> updateNodeList) {
        this.updateNodeList = updateNodeList;
    }
    
    public String getTargetHost() {
        return targetHost;
    }
    
    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        InjectionDTONew that = (InjectionDTONew) object;
        return Objects.equals(createNodeList, that.createNodeList)
                && Objects.equals(updateNodeList, that.updateNodeList)
                && Objects.equals(targetHost, that.targetHost);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(createNodeList, updateNodeList, targetHost);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", InjectionDTONew.class.getSimpleName() + "[", "]")
                .add("createNodeList=" + createNodeList)
                .add("updateNodeList=" + updateNodeList)
                .add("targetHost='" + targetHost + "'")
                .toString();
    }
}
