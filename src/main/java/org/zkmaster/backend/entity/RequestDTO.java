package org.zkmaster.backend.entity;

import java.util.Objects;
import java.util.StringJoiner;

public class RequestDTO {
    private String host;
    private String path;
    private String value;

    public RequestDTO() {
    }

    public RequestDTO(String host, String path) {
        this.host = host;
        this.path = path;
    }

    public RequestDTO(String host, String path, String value) {
        this.host = host;
        this.path = path;
        this.value = value;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
        return value;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RequestDTO that = (RequestDTO) object;
        return Objects.equals(host, that.host)
                && Objects.equals(path, that.path)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, path, value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RequestDTO.class.getSimpleName() + "[", "]")
                .add("host='" + host + "'")
                .add("path='" + path + "'")
                .add("value='" + value + "'")
                .toString();
    }
}
