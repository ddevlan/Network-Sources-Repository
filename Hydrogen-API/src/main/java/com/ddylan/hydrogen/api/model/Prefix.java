package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;

@Document(collection = "prefixes")
public class Prefix {
    @Id
    private String id;
    @Indexed
    private String prefixid;
    @Indexed
    private String displayName;

    public void setId(String id) {
        this.id = id;
    }

    @Indexed
    private String prefix;
    @Indexed
    private boolean purchasable;
    @Indexed
    private String buttonName;
    @Indexed
    private String buttonDescription;

    public void setPrefixid(String prefixid) {
        this.prefixid = prefixid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public void setButtonDescription(String buttonDescription) {
        this.buttonDescription = buttonDescription;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Prefix)) return false;
        Prefix other = (Prefix) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$prefixid = getPrefixid(), other$prefixid = other.getPrefixid();
        if ((this$prefixid == null) ? (other$prefixid != null) : !this$prefixid.equals(other$prefixid)) return false;
        Object this$displayName = getDisplayName(), other$displayName = other.getDisplayName();
        if ((this$displayName == null) ? (other$displayName != null) : !this$displayName.equals(other$displayName))
            return false;
        Object this$prefix = getPrefix(), other$prefix = other.getPrefix();
        if ((this$prefix == null) ? (other$prefix != null) : !this$prefix.equals(other$prefix)) return false;
        if (isPurchasable() != other.isPurchasable()) return false;
        Object this$buttonName = getButtonName(), other$buttonName = other.getButtonName();
        if ((this$buttonName == null) ? (other$buttonName != null) : !this$buttonName.equals(other$buttonName))
            return false;
        Object this$buttonDescription = getButtonDescription(), other$buttonDescription = other.getButtonDescription();
        return !((this$buttonDescription == null) ? (other$buttonDescription != null) : !this$buttonDescription.equals(other$buttonDescription));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Prefix;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $prefixid = getPrefixid();
        result = result * 59 + (($prefixid == null) ? 43 : $prefixid.hashCode());
        Object $displayName = getDisplayName();
        result = result * 59 + (($displayName == null) ? 43 : $displayName.hashCode());
        Object $prefix = getPrefix();
        result = result * 59 + (($prefix == null) ? 43 : $prefix.hashCode());
        result = result * 59 + (isPurchasable() ? 79 : 97);
        Object $buttonName = getButtonName();
        result = result * 59 + (($buttonName == null) ? 43 : $buttonName.hashCode());
        Object $buttonDescription = getButtonDescription();
        return result * 59 + (($buttonDescription == null) ? 43 : $buttonDescription.hashCode());
    }

    public String toString() {
        return "Prefix(id=" + getId() + ", prefixid=" + getPrefixid() + ", displayName=" + getDisplayName() + ", prefix=" + getPrefix() + ", purchasable=" + isPurchasable() + ", buttonName=" + getButtonName() + ", buttonDescription=" + getButtonDescription() + ")";
    }

    @ConstructorProperties({"id", "prefixid", "displayName", "prefix", "purchasable", "buttonName", "buttonDescription"})
    public Prefix(String id, String prefixid, String displayName, String prefix, boolean purchasable, String buttonName, String buttonDescription) {
        this.id = id;
        this.prefixid = prefixid;
        this.displayName = displayName;
        this.prefix = prefix;
        this.purchasable = purchasable;
        this.buttonName = buttonName;
        this.buttonDescription = buttonDescription;
    }

    public Prefix() {
    }

    public String getId() {
        return this.id;
    }

    public String getPrefixid() {
        return this.prefixid;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean isPurchasable() {
        return this.purchasable;
    }

    public String getButtonName() {
        return this.buttonName;
    }

    public String getButtonDescription() {
        return this.buttonDescription;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", this.prefixid);
        json.put("displayName", this.displayName);
        json.put("prefix", this.prefix);
        json.put("purchaseable", Boolean.valueOf(true));
        json.put("buttonName", this.buttonName);
        json.put("buttonDescription", this.buttonDescription);
        return json;
    }
}
