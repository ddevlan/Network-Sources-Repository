package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;
import java.util.Map;

@Document(collection = "punishments")
public class Punishment {
    @Id
    private String id;
    @Indexed
    private String uuid;
    @Indexed
    private String userIp;
    @Indexed
    private String publicReason;
    @Indexed
    private String privateReason;
    @Indexed
    private String type;

    public void setId(String id) {
        this.id = id;
    }

    @Indexed
    private String actorType;
    @Indexed
    private String actorName;
    @Indexed
    private String addedBy;
    @Indexed
    private String addedByIp;
    @Indexed
    private long addedAt;
    @Indexed
    private long expiresAt;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setPublicReason(String publicReason) {
        this.publicReason = publicReason;
    }

    public void setPrivateReason(String privateReason) {
        this.privateReason = privateReason;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setAddedByIp(String addedByIp) {
        this.addedByIp = addedByIp;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRemovedBy(String removedBy) {
        this.removedBy = removedBy;
    }

    public void setRemovedByIp(String removedByIp) {
        this.removedByIp = removedByIp;
    }

    public void setRemovedAt(long removedAt) {
        this.removedAt = removedAt;
    }

    public void setRemovalReason(String removalReason) {
        this.removalReason = removalReason;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Punishment)) return false;
        Punishment other = (Punishment) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$uuid = getUuid(), other$uuid = other.getUuid();
        if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid)) return false;
        Object this$userIp = getUserIp(), other$userIp = other.getUserIp();
        if ((this$userIp == null) ? (other$userIp != null) : !this$userIp.equals(other$userIp)) return false;
        Object this$publicReason = getPublicReason(), other$publicReason = other.getPublicReason();
        if ((this$publicReason == null) ? (other$publicReason != null) : !this$publicReason.equals(other$publicReason))
            return false;
        Object this$privateReason = getPrivateReason(), other$privateReason = other.getPrivateReason();
        if ((this$privateReason == null) ? (other$privateReason != null) : !this$privateReason.equals(other$privateReason))
            return false;
        Object this$type = getType(), other$type = other.getType();
        if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;
        Object this$actorType = getActorType(), other$actorType = other.getActorType();
        if ((this$actorType == null) ? (other$actorType != null) : !this$actorType.equals(other$actorType))
            return false;
        Object this$actorName = getActorName(), other$actorName = other.getActorName();
        if ((this$actorName == null) ? (other$actorName != null) : !this$actorName.equals(other$actorName))
            return false;
        Object this$addedBy = getAddedBy(), other$addedBy = other.getAddedBy();
        if ((this$addedBy == null) ? (other$addedBy != null) : !this$addedBy.equals(other$addedBy)) return false;
        Object this$addedByIp = getAddedByIp(), other$addedByIp = other.getAddedByIp();
        if ((this$addedByIp == null) ? (other$addedByIp != null) : !this$addedByIp.equals(other$addedByIp))
            return false;
        if (getAddedAt() != other.getAddedAt()) return false;
        if (getExpiresAt() != other.getExpiresAt()) return false;
        Object this$removedBy = getRemovedBy(), other$removedBy = other.getRemovedBy();
        if ((this$removedBy == null) ? (other$removedBy != null) : !this$removedBy.equals(other$removedBy))
            return false;
        Object this$removedByIp = getRemovedByIp(), other$removedByIp = other.getRemovedByIp();
        if ((this$removedByIp == null) ? (other$removedByIp != null) : !this$removedByIp.equals(other$removedByIp))
            return false;
        if (getRemovedAt() != other.getRemovedAt()) return false;
        Object this$removalReason = getRemovalReason(), other$removalReason = other.getRemovalReason();
        if ((this$removalReason == null) ? (other$removalReason != null) : !this$removalReason.equals(other$removalReason))
            return false;
        Object this$metadata = getMetadata(), other$metadata = other.getMetadata();
        return !((this$metadata == null) ? (other$metadata != null) : !this$metadata.equals(other$metadata));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Punishment;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $uuid = getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        Object $userIp = getUserIp();
        result = result * 59 + (($userIp == null) ? 43 : $userIp.hashCode());
        Object $publicReason = getPublicReason();
        result = result * 59 + (($publicReason == null) ? 43 : $publicReason.hashCode());
        Object $privateReason = getPrivateReason();
        result = result * 59 + (($privateReason == null) ? 43 : $privateReason.hashCode());
        Object $type = getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $actorType = getActorType();
        result = result * 59 + (($actorType == null) ? 43 : $actorType.hashCode());
        Object $actorName = getActorName();
        result = result * 59 + (($actorName == null) ? 43 : $actorName.hashCode());
        Object $addedBy = getAddedBy();
        result = result * 59 + (($addedBy == null) ? 43 : $addedBy.hashCode());
        Object $addedByIp = getAddedByIp();
        result = result * 59 + (($addedByIp == null) ? 43 : $addedByIp.hashCode());
        long $addedAt = getAddedAt();
        result = result * 59 + (int) ($addedAt >>> 32L ^ $addedAt);
        long $expiresAt = getExpiresAt();
        result = result * 59 + (int) ($expiresAt >>> 32L ^ $expiresAt);
        Object $removedBy = getRemovedBy();
        result = result * 59 + (($removedBy == null) ? 43 : $removedBy.hashCode());
        Object $removedByIp = getRemovedByIp();
        result = result * 59 + (($removedByIp == null) ? 43 : $removedByIp.hashCode());
        long $removedAt = getRemovedAt();
        result = result * 59 + (int) ($removedAt >>> 32L ^ $removedAt);
        Object $removalReason = getRemovalReason();
        result = result * 59 + (($removalReason == null) ? 43 : $removalReason.hashCode());
        Object $metadata = getMetadata();
        return result * 59 + (($metadata == null) ? 43 : $metadata.hashCode());
    }

    public String toString() {
        return "Punishment(id=" + getId() + ", uuid=" + getUuid() + ", userIp=" + getUserIp() + ", publicReason=" + getPublicReason() + ", privateReason=" + getPrivateReason() + ", type=" + getType() + ", actorType=" + getActorType() + ", actorName=" + getActorName() + ", addedBy=" + getAddedBy() + ", addedByIp=" + getAddedByIp() + ", addedAt=" + getAddedAt() + ", expiresAt=" + getExpiresAt() + ", removedBy=" + getRemovedBy() + ", removedByIp=" + getRemovedByIp() + ", removedAt=" + getRemovedAt() + ", removalReason=" + getRemovalReason() + ", metadata=" + getMetadata() + ")";
    }

    @ConstructorProperties({"id", "uuid", "userIp", "publicReason", "privateReason", "type", "actorType", "actorName", "addedBy", "addedByIp", "addedAt", "expiresAt", "removedBy", "removedByIp", "removedAt", "removalReason", "metadata"})
    public Punishment(String id, String uuid, String userIp, String publicReason, String privateReason, String type, String actorType, String actorName, String addedBy, String addedByIp, long addedAt, long expiresAt, String removedBy, String removedByIp, long removedAt, String removalReason, Map<String, String> metadata) {
        this.id = id;
        this.uuid = uuid;
        this.userIp = userIp;
        this.publicReason = publicReason;
        this.privateReason = privateReason;
        this.type = type;
        this.actorType = actorType;
        this.actorName = actorName;
        this.addedBy = addedBy;
        this.addedByIp = addedByIp;
        this.addedAt = addedAt;
        this.expiresAt = expiresAt;
        this.removedBy = removedBy;
        this.removedByIp = removedByIp;
        this.removedAt = removedAt;
        this.removalReason = removalReason;
        this.metadata = metadata;
    }

    public String getId() {
        return this.id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public String getPublicReason() {
        return this.publicReason;
    }

    public String getPrivateReason() {
        return this.privateReason;
    }

    public String getType() {
        return this.type;
    }

    public String getActorType() {
        return this.actorType;
    }

    public String getActorName() {
        return this.actorName;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public String getAddedByIp() {
        return this.addedByIp;
    }

    public long getAddedAt() {
        return this.addedAt;
    }

    public long getExpiresAt() {
        return this.expiresAt;
    }

    @Indexed
    private String removedBy = null;

    public String getRemovedBy() {
        return this.removedBy;
    }

    @Indexed
    private String removedByIp = null;

    public String getRemovedByIp() {
        return this.removedByIp;
    }

    @Indexed
    private long removedAt = 0L;

    public long getRemovedAt() {
        return this.removedAt;
    }

    @Indexed
    private String removalReason = null;
    @Indexed
    private Map<String, String> metadata;

    public String getRemovalReason() {
        return this.removalReason;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public Punishment(String uuid, String userIp, String publicReason, String privateReason, String type, String actorType, String actorName, String addedBy, String addedByIp, long addedAt, long expiresAt, Map<String, String> metadata) {
        this.uuid = uuid;
        this.userIp = userIp;
        this.publicReason = publicReason;
        this.privateReason = privateReason;
        this.type = type;
        this.actorType = actorType;
        this.actorName = actorName;
        this.addedBy = addedBy;
        this.addedByIp = addedByIp;
        this.addedAt = addedAt;
        this.expiresAt = expiresAt;
        this.metadata = metadata;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", this.uuid);
        json.put("publicReason", this.publicReason);
        json.put("privateReason", this.privateReason);
        json.put("type", this.type);
        json.put("actorType", this.actorType);
        json.put("actorName", this.actorName);
        json.put("expiresAt", Long.valueOf((this.expiresAt == -1L) ? 0L : (this.expiresAt * 1000L)));
        System.out.println((this.expiresAt * 1000L) + " " + System.currentTimeMillis());
        json.put("addedAt", Long.valueOf(this.addedAt));

        if (this.addedBy != null) {
            json.put("addedBy", this.addedBy);
        }
        if (this.removedAt != 0L) {
            json.put("removedBy", this.removedBy);
            json.put("removedAt", Long.valueOf(this.removedAt));
            json.put("removalReason", this.removalReason);
        }

        return json;
    }

    public Punishment() {
    }
}
