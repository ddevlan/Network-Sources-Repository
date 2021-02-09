/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.ddylan.library.qLib
 *  com.ddylan.library.util.TPSUtils
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.server.heartbeat;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.hydrogen.util.PermissionUtils;
import com.ddylan.library.LibraryPlugin;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HeartbeatTask
extends BukkitRunnable {
    private boolean first = true;
    private static final Queue<Map<String, Object>> eventQueue = new ConcurrentLinkedQueue<Map<String, Object>>();

    public void run() {
        Map<String, Object> event;
        HashMap<String, HashMap<String, String>> onlinePlayers = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("username", player.getName());
            data.put("userIp", player.getAddress().getAddress().getHostAddress());
            onlinePlayers.put(player.getUniqueId().toString(), data);
        }
        LinkedList<Map<String, Object>> events = new LinkedList<Map<String, Object>>();
        while ((event = eventQueue.poll()) != null) {
            events.add(event);
        }
        RequestResponse response = RequestHandler.post("/servers/heartbeat", ImmutableMap.of("players", onlinePlayers, "lastTps", Bukkit.spigot().getTPS()[0], "events", events, "permissionsNeeded", this.first));
        if (response.wasSuccessful()) {
            JSONObject json = response.asJSONObject();
            JSONObject playersJson = json.getJSONObject("players");
            for (String key : playersJson.keySet()) {
                Player player;
                Profile profile;
                UUID uuid = UUID.fromString(key);
                JSONObject info = playersJson.getJSONObject(key);
                Optional<Profile> profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(uuid);
                if (profileOptional.isPresent()) {
                    profile = profileOptional.get();
                    profile.update(uuid, info);
                    player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    profile.updatePlayer(player);
                    continue;
                }
                profile = new Profile(uuid, info);
                Hydrogen.getInstance().getProfileHandler().getProfiles().put(uuid, profile);
                player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                profile.updatePlayer(player);
            }
            if (json.has("permissions")) {
                Map<?, ?> ret = LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(json.get("permissions").toString(), new TypeToken<Map<String, List<String>>>(){}.getType());
                ConcurrentHashMap<Rank, Map<String, Boolean>> newCache = new ConcurrentHashMap<>();
                for (Map.Entry<?, ?> entry : ret.entrySet()) {
                    newCache.put(Hydrogen.getInstance().getRankHandler().getRank((String)entry.getKey()).get(), PermissionUtils.convertFromList((List<String>) entry.getValue()));
                }
                Hydrogen.getInstance().getPermissionHandler().setPermissionCache(newCache);
            }
        } else {
            Bukkit.getLogger().warning("Heartbeat - Could not POST server heartbeat: " + response.getErrorMessage());
        }
        this.first = false;
    }

    public static Queue<Map<String, Object>> getEventQueue() {
        return eventQueue;
    }
}

