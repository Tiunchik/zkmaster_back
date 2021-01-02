package org.zkmaster.backend.entity.dto;

import org.zkmaster.backend.controllers.APIController;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Use specially for injection node from Source real-server to Other real-server.
 *
 * @see APIController
 */
public class InjectionDTO {
    private String sourceHost;
    private String sourceNodePath;
    private String targetHost;
    private String targetNodePath;
    private boolean updOldValues;

    public InjectionDTO() {
    }

    public InjectionDTO(String sourceHost, String sourceNodePath,
                        String targetHost, String targetNodePath, boolean updOldValues) {
        this.sourceHost = sourceHost;
        this.sourceNodePath = sourceNodePath;
        this.targetHost = targetHost;
        this.targetNodePath = targetNodePath;
        this.updOldValues = updOldValues;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getSourceNodePath() {
        return sourceNodePath;
    }

    public void setSourceNodePath(String sourceNodePath) {
        this.sourceNodePath = sourceNodePath;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public String getTargetNodePath() {
        return targetNodePath;
    }

    public void setTargetNodePath(String targetNodePath) {
        this.targetNodePath = targetNodePath;
    }

    public boolean isUpdOldValues() {
        return updOldValues;
    }

    public void setUpdOldValues(boolean updOldValues) {
        this.updOldValues = updOldValues;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        InjectionDTO injectionDTO = (InjectionDTO) object;
        return updOldValues == injectionDTO.updOldValues
                && Objects.equals(sourceHost, injectionDTO.sourceHost)
                && Objects.equals(sourceNodePath, injectionDTO.sourceNodePath)
                && Objects.equals(targetHost, injectionDTO.targetHost)
                && Objects.equals(targetNodePath, injectionDTO.targetNodePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceHost, sourceNodePath, targetHost, targetNodePath, updOldValues);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InjectionDTO.class.getSimpleName() + "[", "]")
                .add("sourceHost='" + sourceHost + "'")
                .add("sourceNodePath='" + sourceNodePath + "'")
                .add("targetHost='" + targetHost + "'")
                .add("targetNodePath='" + targetNodePath + "'")
                .add("saveOldValues=" + updOldValues)
                .toString();
    }
}
