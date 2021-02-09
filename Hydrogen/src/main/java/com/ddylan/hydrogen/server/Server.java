/*
 * Decompiled with CFR 0.150.
 */
package com.ddylan.hydrogen.server;

import com.ddylan.hydrogen.Hydrogen;

import java.util.Optional;

public class Server {
    private String id;
    private String displayName;
    private String serverGroup;
    private String serverIp;
    private long lastUpdatedAt;
    private double lastTps;

    public ServerGroup resolveGroup() {
        Optional<ServerGroup> groupOptional = Hydrogen.getInstance().getServerHandler().getServerGroup(this.serverGroup);
        return groupOptional.orElse(null);
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
}

