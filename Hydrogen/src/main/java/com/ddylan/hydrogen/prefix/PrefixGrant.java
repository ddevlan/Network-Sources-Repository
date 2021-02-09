/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.util.UUIDUtils
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.prefix;

import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.LibraryPlugin;
import org.json.JSONObject;

import java.util.Set;
import java.util.UUID;

public final class PrefixGrant {
    private final String id;
    private final UUID user;
    private final String reason;
    private final Set<String> scopes;
    private final String prefix;
    private final long expiresAt;
    private final UUID addedBy;
    private final long addedAt;
    private final UUID removedBy;
    private final long removedAt;
    private final String removalReason;

    public boolean isActive() {
        return !this.isExpired() && !this.isRemoved();
    }

    public boolean isExpired() {
        return this.expiresAt != 0L && System.currentTimeMillis() > this.expiresAt;
    }

    public boolean isRemoved() {
        return this.removedAt > 0L;
    }

    public String resolveAddedBy() {
        if (this.addedBy == null) {
            return null;
        }
        if (LibraryPlugin.getInstance().getUuidCache().name(this.addedBy) != null) {
            return LibraryPlugin.getInstance().getUuidCache().name(this.addedBy);
        }
        //TODO: endpoint
        RequestResponse response = RequestHandler.get("/users/" + this.addedBy);
        if (!response.wasSuccessful()) {
            return "Internal error";
        }
        JSONObject json = response.asJSONObject();
        return json.getString("lastUsername");
    }

    public PrefixGrant(String id, UUID user, String reason, Set<String> scopes, String prefix, long expiresAt, UUID addedBy, long addedAt, UUID removedBy, long removedAt, String removalReason) {
        this.id = id;
        this.user = user;
        this.reason = reason;
        this.scopes = scopes;
        this.prefix = prefix;
        this.expiresAt = expiresAt;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.removedBy = removedBy;
        this.removedAt = removedAt;
        this.removalReason = removalReason;
    }

    public String getId() {
        return this.id;
    }

    public UUID getUser() {
        return this.user;
    }

    public String getReason() {
        return this.reason;
    }

    public Set<String> getScopes() {
        return this.scopes;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public long getExpiresAt() {
        return this.expiresAt;
    }

    public UUID getAddedBy() {
        return this.addedBy;
    }

    public long getAddedAt() {
        return this.addedAt;
    }

    public UUID getRemovedBy() {
        return this.removedBy;
    }

    public long getRemovedAt() {
        return this.removedAt;
    }

    public String getRemovalReason() {
        return this.removalReason;
    }
}

