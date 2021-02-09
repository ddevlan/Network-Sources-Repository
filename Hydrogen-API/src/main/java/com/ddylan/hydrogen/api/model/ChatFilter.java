package com.ddylan.hydrogen.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;

@Document(collection = "chatfilter")
public class ChatFilter {

    @Id
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Indexed
    private String regex;

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ChatFilter)) return false;
        ChatFilter other = (ChatFilter) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$regex = getRegex(), other$regex = other.getRegex();
        return !((this$regex == null) ? (other$regex != null) : !this$regex.equals(other$regex));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChatFilter;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $regex = getRegex();
        return result * 59 + (($regex == null) ? 43 : $regex.hashCode());
    }

    public String toString() {
        return "ChatFilter(id=" + getId() + ", regex=" + getRegex() + ")";
    }

    @ConstructorProperties({"id", "regex"})
    public ChatFilter(String id, String regex) {
        this.id = id;
        this.regex = regex;
    }

    public ChatFilter() {
    }

    public String getId() {
        return this.id;
    }

    public String getRegex() {
        return this.regex;
    }
}
