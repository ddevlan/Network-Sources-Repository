package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "players")
public class Player {
    @Id
    private String id;
    @Indexed
    private String uuid;
    @Indexed
    private String username;
    @Indexed
    private String iconColor;
    @Indexed
    private String nameColor;
    @Indexed
    private String activePrefix;
    @Indexed
    private String email;
    @Indexed
    private long lastSeenAt;
    @Indexed
    private String lastSeenOn;
    @Indexed
    private boolean online;
    @Indexed
    private List<String> ranks;
    @Indexed
    private List<String> ipLog;

    public void setId(String id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public void setActivePrefix(String activePrefix) {
        this.activePrefix = activePrefix;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastSeenAt(long lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public void setLastSeenOn(String lastSeenOn) {
        this.lastSeenOn = lastSeenOn;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setRanks(List<String> ranks) {
        this.ranks = ranks;
    }

    public void setIpLog(List<String> ipLog) {
        this.ipLog = ipLog;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Player))
            return false;
        Player other = (Player) o;
        if (!other.canEqual(this))
            return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id))
            return false;
        Object this$uuid = getUuid(), other$uuid = other.getUuid();
        if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid))
            return false;
        Object this$username = getUsername(), other$username = other.getUsername();
        if ((this$username == null) ? (other$username != null) : !this$username.equals(other$username))
            return false;
        Object this$iconColor = getIconColor(), other$iconColor = other.getIconColor();
        if ((this$iconColor == null) ? (other$iconColor != null) : !this$iconColor.equals(other$iconColor))
            return false;
        Object this$nameColor = getNameColor(), other$nameColor = other.getNameColor();
        if ((this$nameColor == null) ? (other$nameColor != null) : !this$nameColor.equals(other$nameColor))
            return false;
        Object this$activePrefix = getActivePrefix(), other$activePrefix = other.getActivePrefix();
        if ((this$activePrefix == null) ? (other$activePrefix != null) : !this$activePrefix.equals(other$activePrefix))
            return false;
        Object this$email = getEmail(), other$email = other.getEmail();
        if ((this$email == null) ? (other$email != null) : !this$email.equals(other$email))
            return false;
        if (getLastSeenAt() != other.getLastSeenAt())
            return false;
        Object this$lastSeenOn = getLastSeenOn(), other$lastSeenOn = other.getLastSeenOn();
        if ((this$lastSeenOn == null) ? (other$lastSeenOn != null) : !this$lastSeenOn.equals(other$lastSeenOn))
            return false;
        if (isOnline() != other.isOnline())
            return false;
        List<String> this$ranks = getRanks(), other$ranks = other.getRanks();
        if ((this$ranks == null) ? (other$ranks != null) : !this$ranks.equals(other$ranks))
            return false;
        List<String> this$ipLog = getIpLog(), other$ipLog = other.getIpLog();
        return !((this$ipLog == null) ? (other$ipLog != null) : !this$ipLog.equals(other$ipLog));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Player;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $uuid = getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        Object $username = getUsername();
        result = result * 59 + (($username == null) ? 43 : $username.hashCode());
        Object $iconColor = getIconColor();
        result = result * 59 + (($iconColor == null) ? 43 : $iconColor.hashCode());
        Object $nameColor = getNameColor();
        result = result * 59 + (($nameColor == null) ? 43 : $nameColor.hashCode());
        Object $activePrefix = getActivePrefix();
        result = result * 59 + (($activePrefix == null) ? 43 : $activePrefix.hashCode());
        Object $email = getEmail();
        result = result * 59 + (($email == null) ? 43 : $email.hashCode());
        long $lastSeenAt = getLastSeenAt();
        result = result * 59 + (int) ($lastSeenAt >>> 32L ^ $lastSeenAt);
        Object $lastSeenOn = getLastSeenOn();
        result = result * 59 + (($lastSeenOn == null) ? 43 : $lastSeenOn.hashCode());
        result = result * 59 + (isOnline() ? 79 : 97);
        List<String> $ranks = getRanks();
        result = result * 59 + (($ranks == null) ? 43 : $ranks.hashCode());
        List<String> $ipLog = getIpLog();
        return result * 59 + (($ipLog == null) ? 43 : $ipLog.hashCode());
    }

    public String toString() {
        return "Player(id=" + getId() + ", uuid=" + getUuid() + ", username=" + getUsername() + ", iconColor=" + getIconColor() + ", nameColor=" + getNameColor() + ", activePrefix=" + getActivePrefix() + ", email=" + getEmail() + ", lastSeenAt=" + getLastSeenAt() + ", lastSeenOn=" + getLastSeenOn() + ", online=" + isOnline() + ", ranks=" + getRanks() + ", ipLog=" + getIpLog() + ")";
    }

    @ConstructorProperties({"id", "uuid", "username", "iconColor", "nameColor", "activePrefix", "email", "lastSeenAt", "lastSeenOn", "online", "ranks", "ipLog"})
    public Player(String id, String uuid, String username, String iconColor, String nameColor, String activePrefix, String email, long lastSeenAt, String lastSeenOn, boolean online, List<String> ranks, List<String> ipLog) {
        this.online = false;
        this.ranks = new ArrayList<>();

        this.ipLog = new ArrayList<>();
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.iconColor = iconColor;
        this.nameColor = nameColor;
        this.activePrefix = activePrefix;
        this.email = email;
        this.lastSeenAt = lastSeenAt;
        this.lastSeenOn = lastSeenOn;
        this.online = online;
        this.ranks = ranks;
        this.ipLog = ipLog;
    }

    public Player() {
        this.online = false;
        this.ranks = new ArrayList<>();
        this.ipLog = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getIconColor() {
        return this.iconColor;
    }

    public String getNameColor() {
        return this.nameColor;
    }

    public List<String> getIpLog() {
        return this.ipLog;
    }

    public String getActivePrefix() {
        return this.activePrefix;
    }

    public String getEmail() {
        return this.email;
    }

    public long getLastSeenAt() {
        return this.lastSeenAt;
    }

    public String getLastSeenOn() {
        return this.lastSeenOn;
    }

    public boolean isOnline() {
        return this.online;
    }

    public List<String> getRanks() {
        return this.ranks;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("uuid", this.uuid);
        json.put("username", this.username);
        json.put("online", Boolean.valueOf(this.online));
        json.put("lastSeenOn", this.lastSeenOn);
        json.put("lastSeenAt", Long.valueOf(this.lastSeenAt));
        json.put("ranks", this.ranks);

        json.put("ipLog", this.ipLog);

        if (this.iconColor != null) {
            json.put("iconColor", this.iconColor);
        }
        if (this.nameColor != null) {
            json.put("nameColor", this.nameColor);
        }
        if (this.activePrefix != null) {
            json.put("activePrefix", this.activePrefix);
        }
        return json;
    }
}
