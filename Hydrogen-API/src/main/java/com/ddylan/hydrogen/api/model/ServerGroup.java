package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;

@Document(collection = "servergroups")
public class ServerGroup {

    @Id
    private String _id;

    public void set_id(String _id) {
        this._id = _id;
    }

    @Indexed
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerGroup)) return false;
        ServerGroup other = (ServerGroup) o;
        if (!other.canEqual(this)) return false;
        Object this$_id = get_id(), other$_id = other.get_id();
        if ((this$_id == null) ? (other$_id != null) : !this$_id.equals(other$_id)) return false;
        Object this$id = getId(), other$id = other.getId();
        return !((this$id == null) ? (other$id != null) : !this$id.equals(other$id));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerGroup;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $_id = get_id();
        result = result * 59 + (($_id == null) ? 43 : $_id.hashCode());
        Object $id = getId();
        return result * 59 + (($id == null) ? 43 : $id.hashCode());
    }

    public String toString() {
        return "ServerGroup(_id=" + get_id() + ", id=" + getId() + ")";
    }

    @ConstructorProperties({"_id", "id"})
    public ServerGroup(String _id, String id) {
        this._id = _id;
        this.id = id;
    }

    public ServerGroup() {
    }

    public String get_id() {
        return this._id;
    }

    public String getId() {
        return this.id;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("image", "");
        json.put("announcements", new JSONArray());
        return json;
    }
}
