package org.zkmaster.backend.entity.dto;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class TransformDTO {
    private String type;
    private List<String> content;

    public TransformDTO() {
    }

    public TransformDTO(String type, List<String> content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
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
        return Objects.equals(type, that.type)
                && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransformDTO.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("content=" + content)
                .toString();
    }
}
