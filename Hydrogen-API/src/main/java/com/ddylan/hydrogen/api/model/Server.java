package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;

import java.beans.ConstructorProperties;

public class Server {

    private String id;
    private String displayName;
    private String serverGroup;

    public void setId(String id) {
        this.id = id;
    }

    private String serverIp;
    private long lastUpdatedAt;
    private double lastTps;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setServerGroup(String serverGroup) {
        this.serverGroup = serverGroup;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setLastUpdatedAt(long lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void setLastTps(double lastTps) {
        this.lastTps = lastTps;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Server)) return false;
        Server other = (Server) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$displayName = getDisplayName(), other$displayName = other.getDisplayName();
        if ((this$displayName == null) ? (other$displayName != null) : !this$displayName.equals(other$displayName))
            return false;
        Object this$serverGroup = getServerGroup(), other$serverGroup = other.getServerGroup();
        if ((this$serverGroup == null) ? (other$serverGroup != null) : !this$serverGroup.equals(other$serverGroup))
            return false;
        Object this$serverIp = getServerIp(), other$serverIp = other.getServerIp();
        return ((this$serverIp == null) ? (other$serverIp != null) : !this$serverIp.equals(other$serverIp)) ? false : ((getLastUpdatedAt() != other.getLastUpdatedAt()) ? false : (!(Double.compare(getLastTps(), other.getLastTps()) != 0)));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Server;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $displayName = getDisplayName();
        result = result * 59 + (($displayName == null) ? 43 : $displayName.hashCode());
        Object $serverGroup = getServerGroup();
        result = result * 59 + (($serverGroup == null) ? 43 : $serverGroup.hashCode());
        Object $serverIp = getServerIp();
        result = result * 59 + (($serverIp == null) ? 43 : $serverIp.hashCode());
        long $lastUpdatedAt = getLastUpdatedAt();
        result = result * 59 + (int) ($lastUpdatedAt >>> 32L ^ $lastUpdatedAt);
        long $lastTps = Double.doubleToLongBits(getLastTps());
        return result * 59 + (int) ($lastTps >>> 32L ^ $lastTps);
    }

    public String toString() {
        return "Server(id=" + getId() + ", displayName=" + getDisplayName() + ", serverGroup=" + getServerGroup() + ", serverIp=" + getServerIp() + ", lastUpdatedAt=" + getLastUpdatedAt() + ", lastTps=" + getLastTps() + ")";
    }

    @ConstructorProperties({"id", "displayName", "serverGroup", "serverIp", "lastUpdatedAt", "lastTps"})
    public Server(String id, String displayName, String serverGroup, String serverIp, long lastUpdatedAt, double lastTps) {
        this.id = id;
        this.displayName = displayName;
        this.serverGroup = serverGroup;
        this.serverIp = serverIp;
        this.lastUpdatedAt = lastUpdatedAt;
        this.lastTps = lastTps;
    }

    public Server() {
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getServerGroup() {
        return this.serverGroup;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public long getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }

    public double getLastTps() {
        return this.lastTps;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("displayName", this.displayName);
        json.put("serverGroup", this.serverGroup);
        json.put("serverIp", this.serverIp);
        json.put("lastUpdatedAt", Long.valueOf(this.lastUpdatedAt));
        json.put("lastTps", Double.valueOf(this.lastTps));
        return json;
    }
}
