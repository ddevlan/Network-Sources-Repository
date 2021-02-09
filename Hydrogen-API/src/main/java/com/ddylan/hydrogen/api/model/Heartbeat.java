package com.ddylan.hydrogen.api.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;

@Document(collection = "heartbeat")
public class Heartbeat {

    @Id
    private String id;
    private JSONObject players;

    public void setId(String id) {
        this.id = id;
    }

    private List<Map<String, String>> events;
    private double lastTps;
    private boolean permissionsNeeded;

    public void setPlayers(JSONObject players) {
        this.players = players;
    }

    public void setEvents(List<Map<String, String>> events) {
        this.events = events;
    }

    public void setLastTps(double lastTps) {
        this.lastTps = lastTps;
    }

    public void setPermissionsNeeded(boolean permissionsNeeded) {
        this.permissionsNeeded = permissionsNeeded;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Heartbeat)) return false;
        Heartbeat other = (Heartbeat) o;
        if (!other.canEqual(this)) return false;
        Object this$id = getId(), other$id = other.getId();
        if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;
        Object this$players = getPlayers(), other$players = other.getPlayers();
        if ((this$players == null) ? (other$players != null) : !this$players.equals(other$players)) return false;
        Object this$events = getEvents(), other$events = other.getEvents();
        return ((this$events == null) ? (other$events == null) : this$events.equals(other$events)) && (Double.compare(getLastTps(), other.getLastTps()) == 0 && (!(isPermissionsNeeded() != other.isPermissionsNeeded())));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Heartbeat;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $players = getPlayers();
        result = result * 59 + (($players == null) ? 43 : $players.hashCode());
        Object $events = getEvents();
        result = result * 59 + (($events == null) ? 43 : $events.hashCode());
        long $lastTps = Double.doubleToLongBits(getLastTps());
        result = result * 59 + (int) ($lastTps >>> 32L ^ $lastTps);
        return result * 59 + (isPermissionsNeeded() ? 79 : 97);
    }

    public String toString() {
        return "Heartbeat(id=" + getId() + ", players=" + getPlayers() + ", events=" + getEvents() + ", lastTps=" + getLastTps() + ", permissionsNeeded=" + isPermissionsNeeded() + ")";
    }

    @ConstructorProperties({"id", "players", "events", "lastTps", "permissionsNeeded"})
    public Heartbeat(String id, JSONObject players, List<Map<String, String>> events, double lastTps, boolean permissionsNeeded) {
        this.id = id;
        this.players = players;
        this.events = events;
        this.lastTps = lastTps;
        this.permissionsNeeded = permissionsNeeded;
    }


    public Heartbeat() {
    }

    public String getId() {
        return this.id;
    }

    public JSONObject getPlayers() {
        return this.players;
    }

    public List<Map<String, String>> getEvents() {
        return this.events;
    }

    public double getLastTps() {
        return this.lastTps;
    }

    public boolean isPermissionsNeeded() {
        return this.permissionsNeeded;
    }

    public Heartbeat(JSONObject players, List<Map<String, String>> events, double lastTps, boolean permissionsNeeded) {
        this.players = players;
        this.events = events;
        this.lastTps = lastTps;
        this.permissionsNeeded = permissionsNeeded;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("players", this.players);
        json.put("events", this.events);
        json.put("lastTps", Double.valueOf(this.lastTps));
        json.put("permissionsNeeded", Boolean.valueOf(this.permissionsNeeded));
        return json;
    }
}
