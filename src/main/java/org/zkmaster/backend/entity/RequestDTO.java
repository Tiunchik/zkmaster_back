package org.zkmaster.backend.entity;

import java.util.Objects;
import java.util.StringJoiner;

public class RequestDTO {
    private String path;
    private String value;
    private String name;

    public RequestDTO() {
    }

    public RequestDTO(String path, String value, String name) {
        this.path = path;
        this.value = value;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
        return value;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(path, that.path) &&
                Objects.equals(value, that.value) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, value, name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RequestDTO.class.getSimpleName() + "[", "]")
                .add("path='" + path + "'")
                .add("value='" + value + "'")
                .add("name='" + name + "'")
                .toString();
    }

}
