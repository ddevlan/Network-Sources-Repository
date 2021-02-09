package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "ranks")
public class Rank {

    @Id
    private String id;
    @Indexed
    private String rankid;
    @Indexed
    private String inheritsFromId;
    @Indexed
    private int generalWeight;
    @Indexed
    private int displayWeight;
    @Indexed
    private String displayName;
    @Indexed
    private String gamePrefix;
    @Indexed
    private String gameColor;
    @Indexed
    private boolean staffRank;
    @Indexed
    private boolean grantRequiresTotp;
    @Indexed
    private String queueMessage;
    @Indexed
    private List<String> permissions;

    public void setId(String id) {
        this.id = id;
    }

    public void setRankid(String rankid) {
        this.rankid = rankid;
    }

    public void setInheritsFromId(String inheritsFromId) {
        this.inheritsFromId = inheritsFromId;
    }

    public void setGeneralWeight(int generalWeight) {
        this.generalWeight = generalWeight;
    }

    public void setDisplayWeight(int displayWeight) {
        this.displayWeight = displayWeight;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGamePrefix(String gamePrefix) {
        this.gamePrefix = gamePrefix;
    }

    public void setGameColor(String gameColor) {
        this.gameColor = gameColor;
    }

    public void setStaffRank(boolean staffRank) {
        this.staffRank = staffRank;
    }

    public void setGrantRequiresTotp(boolean grantRequiresTotp) {
        this.grantRequiresTotp = grantRequiresTotp;
    }

    public void setQueueMessage(String queueMessage) {
        this.queueMessage = queueMessage;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Rank)) return false;
        Rank other = (Rank) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$rankid = getRankid(), other$rankid = other.getRankid();
        if ((this$rankid == null) ? (other$rankid != null) : !this$rankid.equals(other$rankid)) return false;
        Object this$inheritsFromId = getInheritsFromId(), other$inheritsFromId = other.getInheritsFromId();
        if ((this$inheritsFromId == null) ? (other$inheritsFromId != null) : !this$inheritsFromId.equals(other$inheritsFromId))
            return false;
        if (getGeneralWeight() != other.getGeneralWeight()) return false;
        if (getDisplayWeight() != other.getDisplayWeight()) return false;
        Object this$displayName = getDisplayName(), other$displayName = other.getDisplayName();
        if ((this$displayName == null) ? (other$displayName != null) : !this$displayName.equals(other$displayName))
            return false;
        Object this$gamePrefix = getGamePrefix(), other$gamePrefix = other.getGamePrefix();
        if ((this$gamePrefix == null) ? (other$gamePrefix != null) : !this$gamePrefix.equals(other$gamePrefix))
            return false;
        Object this$gameColor = getGameColor(), other$gameColor = other.getGameColor();
        if ((this$gameColor == null) ? (other$gameColor != null) : !this$gameColor.equals(other$gameColor))
            return false;
        if (isStaffRank() != other.isStaffRank()) return false;
        if (isGrantRequiresTotp() != other.isGrantRequiresTotp()) return false;
        Object this$queueMessage = getQueueMessage(), other$queueMessage = other.getQueueMessage();
        if ((this$queueMessage == null) ? (other$queueMessage != null) : !this$queueMessage.equals(other$queueMessage))
            return false;
        Object this$permissions = getPermissions(), other$permissions = other.getPermissions();
        return !((this$permissions == null) ? (other$permissions != null) : !this$permissions.equals(other$permissions));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Rank;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $rankid = getRankid();
        result = result * 59 + (($rankid == null) ? 43 : $rankid.hashCode());
        Object $inheritsFromId = getInheritsFromId();
        result = result * 59 + (($inheritsFromId == null) ? 43 : $inheritsFromId.hashCode());
        result = result * 59 + getGeneralWeight();
        result = result * 59 + getDisplayWeight();
        Object $displayName = getDisplayName();
        result = result * 59 + (($displayName == null) ? 43 : $displayName.hashCode());
        Object $gamePrefix = getGamePrefix();
        result = result * 59 + (($gamePrefix == null) ? 43 : $gamePrefix.hashCode());
        Object $gameColor = getGameColor();
        result = result * 59 + (($gameColor == null) ? 43 : $gameColor.hashCode());
        result = result * 59 + (isStaffRank() ? 79 : 97);
        result = result * 59 + (isGrantRequiresTotp() ? 79 : 97);
        Object $queueMessage = getQueueMessage();
        result = result * 59 + (($queueMessage == null) ? 43 : $queueMessage.hashCode());
        Object $permissions = getPermissions();
        return result * 59 + (($permissions == null) ? 43 : $permissions.hashCode());
    }

    public String toString() {
        return "Rank(id=" + getId() + ", rankid=" + getRankid() + ", inheritsFromId=" + getInheritsFromId() + ", generalWeight=" + getGeneralWeight() + ", displayWeight=" + getDisplayWeight() + ", displayName=" + getDisplayName() + ", gamePrefix=" + getGamePrefix() + ", gameColor=" + getGameColor() + ", staffRank=" + isStaffRank() + ", grantRequiresTotp=" + isGrantRequiresTotp() + ", queueMessage=" + getQueueMessage() + ", permissions=" + getPermissions() + ")";
    }


    @ConstructorProperties({"id", "rankid", "inheritsFromId", "generalWeight", "displayWeight", "displayName", "gamePrefix", "gameColor", "staffRank", "grantRequiresTotp", "queueMessage", "permissions"})
    public Rank(String id, String rankid, String inheritsFromId, int generalWeight, int displayWeight, String displayName, String gamePrefix, String gameColor, boolean staffRank, boolean grantRequiresTotp, String queueMessage, List<String> permissions) {
        this.permissions = new ArrayList<>();
        this.id = id;
        this.rankid = rankid;
        this.inheritsFromId = inheritsFromId;
        this.generalWeight = generalWeight;
        this.displayWeight = displayWeight;
        this.displayName = displayName;
        this.gamePrefix = gamePrefix;
        this.gameColor = gameColor;
        this.staffRank = staffRank;
        this.grantRequiresTotp = grantRequiresTotp;
        this.queueMessage = queueMessage;
        this.permissions = permissions;
    }

    public Rank() {
        this.permissions = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getRankid() {
        return this.rankid;
    }

    public String getInheritsFromId() {
        return this.inheritsFromId;
    }

    public int getGeneralWeight() {
        return this.generalWeight;
    }

    public int getDisplayWeight() {
        return this.displayWeight;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getGamePrefix() {
        return this.gamePrefix;
    }

    public String getGameColor() {
        return this.gameColor;
    }

    public boolean isStaffRank() {
        return this.staffRank;
    }

    public boolean isGrantRequiresTotp() {
        return this.grantRequiresTotp;
    }

    public String getQueueMessage() {
        return this.queueMessage;
    }

    public JSONObject toJSON() {
        JSONObject rank = new JSONObject();
        rank.put("id", this.rankid);
        rank.put("inheritsFromId", this.inheritsFromId);
        rank.put("generalWeight", Integer.valueOf(this.generalWeight));
        rank.put("displayWeight", Integer.valueOf(this.displayWeight));
        rank.put("displayName", this.displayName);
        rank.put("gamePrefix", this.gamePrefix);
        rank.put("gameColor", this.gameColor);
        rank.put("staffRank", Boolean.valueOf(this.staffRank));
        rank.put("grantRequiredTotp", Boolean.valueOf(this.grantRequiresTotp));
        rank.put("queueMessage", this.queueMessage);
        rank.put("permissions", this.permissions);
        return rank;
    }

}
