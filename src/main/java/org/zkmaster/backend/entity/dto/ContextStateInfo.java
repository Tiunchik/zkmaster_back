package org.zkmaster.backend.entity.dto;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Statistic and logging DTO, for see runtime state.
 */
public class ContextStateInfo {
    int contextSize;
    Map<String, Integer> hostsAndNodeCount;
    
    public ContextStateInfo() {
    }
    
    public ContextStateInfo(int contextSize, Map<String, Integer> hostsAndNodeCount) {
        this.contextSize = contextSize;
        this.hostsAndNodeCount = hostsAndNodeCount;
    }
    
    public int getContextSize() {
        return contextSize;
    }
    
    public void setContextSize(int contextSize) {
        this.contextSize = contextSize;
    }
    
    public Map<String, Integer> getHostsAndNodeCount() {
        return hostsAndNodeCount;
    }
    
    public void setHostsAndNodeCount(Map<String, Integer> hostsAndNodeCount) {
        this.hostsAndNodeCount = hostsAndNodeCount;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ContextStateInfo that = (ContextStateInfo) object;
        return contextSize == that.contextSize
                && Objects.equals(hostsAndNodeCount, that.hostsAndNodeCount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(contextSize, hostsAndNodeCount);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ContextStateInfo.class.getSimpleName() + "[", "]")
                .add("contextSize=" + contextSize)
                .add("hostsAndNodeCount=" + hostsAndNodeCount)
                .toString();
    }
}
