package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;
import java.util.List;

@Document(collection = "grants")
public class RankGrant {
    @Id
    private String id;
    @Indexed
    private String uuid;
    @Indexed
    private String reason;
    @Indexed
    private String rank;
    @Indexed
    private long expiresIn;

    public void setId(String id) {
        this.id = id;
    }

    @Indexed
    private long addedAt;
    @Indexed
    private String addedBy;
    @Indexed
    private String addedByIp;
    @Indexed
    private String removedBy;
    @Indexed
    private String removedByIp;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setAddedByIp(String addedByIp) {
        this.addedByIp = addedByIp;
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

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RankGrant)) return false;
        RankGrant other = (RankGrant) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$uuid = getUuid(), other$uuid = other.getUuid();
        if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid)) return false;
        Object this$reason = getReason(), other$reason = other.getReason();
        if ((this$reason == null) ? (other$reason != null) : !this$reason.equals(other$reason)) return false;
        Object this$rank = getRank(), other$rank = other.getRank();
        if ((this$rank == null) ? (other$rank != null) : !this$rank.equals(other$rank)) return false;
        if (getExpiresIn() != other.getExpiresIn()) return false;
        if (getAddedAt() != other.getAddedAt()) return false;
        Object this$addedBy = getAddedBy(), other$addedBy = other.getAddedBy();
        if ((this$addedBy == null) ? (other$addedBy != null) : !this$addedBy.equals(other$addedBy)) return false;
        Object this$addedByIp = getAddedByIp(), other$addedByIp = other.getAddedByIp();
        if ((this$addedByIp == null) ? (other$addedByIp != null) : !this$addedByIp.equals(other$addedByIp))
            return false;
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
        Object this$scopes = getScopes(), other$scopes = other.getScopes();
        return !((this$scopes == null) ? (other$scopes != null) : !this$scopes.equals(other$scopes));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RankGrant;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $uuid = getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        Object $reason = getReason();
        result = result * 59 + (($reason == null) ? 43 : $reason.hashCode());
        Object $rank = getRank();
        result = result * 59 + (($rank == null) ? 43 : $rank.hashCode());
        long $expiresIn = getExpiresIn();
        result = result * 59 + (int) ($expiresIn >>> 32L ^ $expiresIn);
        long $addedAt = getAddedAt();
        result = result * 59 + (int) ($addedAt >>> 32L ^ $addedAt);
        Object $addedBy = getAddedBy();
        result = result * 59 + (($addedBy == null) ? 43 : $addedBy.hashCode());
        Object $addedByIp = getAddedByIp();
        result = result * 59 + (($addedByIp == null) ? 43 : $addedByIp.hashCode());
        Object $removedBy = getRemovedBy();
        result = result * 59 + (($removedBy == null) ? 43 : $removedBy.hashCode());
        Object $removedByIp = getRemovedByIp();
        result = result * 59 + (($removedByIp == null) ? 43 : $removedByIp.hashCode());
        long $removedAt = getRemovedAt();
        result = result * 59 + (int) ($removedAt >>> 32L ^ $removedAt);
        Object $removalReason = getRemovalReason();
        result = result * 59 + (($removalReason == null) ? 43 : $removalReason.hashCode());
        Object $scopes = getScopes();
        return result * 59 + (($scopes == null) ? 43 : $scopes.hashCode());
    }

    public String toString() {
        return "RankGrant(id=" + getId() + ", uuid=" + getUuid() + ", reason=" + getReason() + ", rank=" + getRank() + ", expiresIn=" + getExpiresIn() + ", addedAt=" + getAddedAt() + ", addedBy=" + getAddedBy() + ", addedByIp=" + getAddedByIp() + ", removedBy=" + getRemovedBy() + ", removedByIp=" + getRemovedByIp() + ", removedAt=" + getRemovedAt() + ", removalReason=" + getRemovalReason() + ", scopes=" + getScopes() + ")";
    }

    @ConstructorProperties({"id", "uuid", "reason", "rank", "expiresIn", "addedAt", "addedBy", "addedByIp", "removedBy", "removedByIp", "removedAt", "removalReason", "scopes"})
    public RankGrant(String id, String uuid, String reason, String rank, long expiresIn, long addedAt, String addedBy, String addedByIp, String removedBy, String removedByIp, long removedAt, String removalReason, List<String> scopes) {
        this.id = id;
        this.uuid = uuid;
        this.reason = reason;
        this.rank = rank;
        this.expiresIn = expiresIn;
        this.addedAt = addedAt;
        this.addedBy = addedBy;
        this.addedByIp = addedByIp;
        this.removedBy = removedBy;
        this.removedByIp = removedByIp;
        this.removedAt = removedAt;
        this.removalReason = removalReason;
        this.scopes = scopes;
    }

    public String getId() {
        return this.id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getReason() {
        return this.reason;
    }

    public String getRank() {
        return this.rank;
    }

    public long getExpiresIn() {
        return this.expiresIn;
    }

    public long getAddedAt() {
        return this.addedAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public String getAddedByIp() {
        return this.addedByIp;
    }

    public String getRemovedBy() {
        return this.removedBy;
    }

    public String getRemovedByIp() {
        return this.removedByIp;
    }

    @Indexed
    private long removedAt = 0L;
    @Indexed
    private String removalReason;
    @Indexed
    private List<String> scopes;

    public long getRemovedAt() {
        return this.removedAt;
    }

    public String getRemovalReason() {
        return this.removalReason;
    }

    public List<String> getScopes() {
        return this.scopes;
    }

    public RankGrant(String uuid, String reason, String rank, List<String> scopes, long expiresIn, long addedAt, String addedBy, String addedByIp) {
        this.uuid = uuid;
        this.reason = reason;
        this.rank = rank;
        this.scopes = scopes;
        this.expiresIn = expiresIn;
        this.addedAt = addedAt;
        this.addedBy = addedBy;
        this.addedByIp = addedByIp;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("uuid", this.uuid);
        json.put("reason", this.reason);
        json.put("rank", this.rank);
        json.put("scopes", this.scopes);
        json.put("expiresAt", (this.expiresIn == -1L) ? 0L : (this.expiresIn * 1000L));
        json.put("addedAt", this.addedAt * 1000L);

        if (this.addedBy != null && this.addedByIp != null) {
            json.put("addedBy", this.addedBy);
            json.put("addedByIp", this.addedByIp);
        }

        if (this.removedAt != 0L) {
            json.put("removedBy", this.removedBy);
            json.put("removedByIp", this.removedByIp);
            json.put("removalReason", this.removalReason);
            json.put("removedAt", this.removedAt * 1000L);
        }

        return json;
    }

    public RankGrant() {
    }
}
