package org.zkmaster.backend.entity.dto;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Use specially for copy sub-node from Source real-server to Other real-server.
 */
public class CopyDTO {
    private String sourceHost;
    private String sourceNodePath;
    private String targetHost;
    private String targetNodePath;

    public CopyDTO() {
    }

    public CopyDTO(String sourceHost, String sourceNodePath, String targetHost, String targetNodePath) {
        this.sourceHost = sourceHost;
        this.sourceNodePath = sourceNodePath;
        this.targetHost = targetHost;
        this.targetNodePath = targetNodePath;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        CopyDTO copyDTO = (CopyDTO) object;
        return Objects.equals(sourceHost, copyDTO.sourceHost)
                && Objects.equals(sourceNodePath, copyDTO.sourceNodePath)
                && Objects.equals(targetHost, copyDTO.targetHost)
                && Objects.equals(targetNodePath, copyDTO.targetNodePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceHost, sourceNodePath, targetHost, targetNodePath);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CopyDTO.class.getSimpleName() + "[", "]")
                .add("sourceHost='" + sourceHost + "'")
                .add("sourceNodePath='" + sourceNodePath + "'")
                .add("targetHost='" + targetHost + "'")
                .add("targetNodePath='" + targetNodePath + "'")
                .toString();
    }
}
