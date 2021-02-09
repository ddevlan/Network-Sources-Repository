/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 *  com.ddylan.library.qLib
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.profile;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.LibraryPlugin;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.util.*;

public class ProfileHandler {
    private Set<UUID> totpEnabled = new HashSet<UUID>();
    private final Map<UUID, Profile> profiles = Maps.newConcurrentMap();

    public ProfileHandler() {
        this.refresh();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hydrogen.getInstance(), this::refresh, 0L, 2400L);
    }

    private void refresh() {
        //TODO: endpoint
        RequestResponse response = RequestHandler.get("/dumps/totp");
        if (response.wasSuccessful()) {
            this.totpEnabled = LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<Set<UUID>>(){}.getType());
        } else {
            Bukkit.getLogger().warning("ProfileHandler - Could not get totp-enabled users from API: " + response.getErrorMessage());
        }
    }

    public void remove(UUID player) {
        this.profiles.remove(player);
    }

    public Optional<Profile> getProfile(UUID player) {
        return Optional.ofNullable(this.profiles.get(player));
    }

    public Profile loadProfile(UUID player, String name, String ip) {
        RequestResponse response = RequestHandler.post("/users/" + player.toString() + "/login", ImmutableMap.of("username", name, "userIp", ip));
        if (response.wasSuccessful()) {
            JSONObject json = response.asJSONObject();
            Profile profile = new Profile(player, json);
            this.profiles.put(player, profile);
            return profile;
        }
        Bukkit.getLogger().warning("ProfileHandler - Could not load profile for " + player.toString() + ": " + response.getErrorMessage());
        return null;
    }

    public Set<UUID> getTotpEnabled() {
        return this.totpEnabled;
    }

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }
}

