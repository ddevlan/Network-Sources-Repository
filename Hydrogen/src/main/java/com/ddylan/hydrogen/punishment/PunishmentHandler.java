/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.ddylan.library.qLib
 *  okhttp3.Request$Builder
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.punishment;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.hydrogen.punishment.meta.PunishmentMetaFetcher;
import com.ddylan.hydrogen.punishment.meta.defaults.HPunishmentMetaFetcher;
import com.ddylan.library.LibraryPlugin;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import okhttp3.Request;
import org.bukkit.Bukkit;

import java.util.*;

public class PunishmentHandler {
    private final List<Request.Builder> requestQueue = Lists.newArrayList();
    private final List<PunishmentMetaFetcher> metaFetchers = Lists.newArrayList();

    public PunishmentHandler() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hydrogen.getInstance(), this::refresh, 0L, 6000L);
        this.registerMetaFetcher(new HPunishmentMetaFetcher());
    }

    private void refresh() {
        if (this.requestQueue.size() != 0) {
            try {
                int amount = this.requestQueue.size();
                Iterator<Request.Builder> iterator = this.requestQueue.iterator();
                while (iterator.hasNext()) {
                    Request.Builder builder = iterator.next();
                    RequestResponse secondResponse = RequestHandler.send(builder);
                    if (secondResponse.couldNotConnect()) continue;
                    iterator.remove();
                }
                Bukkit.getLogger().info("PunishmentCache - Flushed " + amount + " queued requests.");
            }
            catch (Exception e) {
                e.printStackTrace();
                Bukkit.getLogger().warning("PunishmentCache - Could not flush requests: " + e.getMessage());
            }
        }
    }

    public RequestResponse punish(UUID player, UUID sender, Punishment.PunishmentType type, String publicReason, String privateReason, long expiresIn) {
        Optional<Profile> profileOptional;
        if ((type == Punishment.PunishmentType.BAN || type == Punishment.PunishmentType.BLACKLIST) && Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player).hasMetadata("HAL-Recording")) {
            return new RequestResponse(false, "This player cannot be banned at this time.", "", null);
        }
        HashMap metaMap = Maps.newHashMap();
        for (PunishmentMetaFetcher fetcher : this.metaFetchers) {
            metaMap.put(fetcher.getPlugin().getName(), this.fixMeta(fetcher.fetch(player, type).getInfo()));
        }
        HashMap body = Maps.newHashMap();
        body.put("user", player.toString());
        body.put("publicReason", publicReason);
        body.put("privateReason", privateReason);
        body.put("type", type.toString());
        if (expiresIn > 0L) {
            body.put("expiresIn", expiresIn);
        }
        if (Bukkit.getPlayer(sender) != null) {
            body.put("addedBy", sender.toString());
            body.put("addedByIp", Bukkit.getPlayer(sender).getAddress().getAddress().getHostAddress());
        }
        if (Bukkit.getPlayer(player) != null) {
            String ip = Bukkit.getPlayer(player).getAddress().getAddress().getHostAddress();
            body.put("userIp", ip);
        }
        body.put("metadata", metaMap);
        RequestResponse response = RequestHandler.post("/punishments", body);
        if (!response.wasSuccessful()) {
            Bukkit.getLogger().warning("PunishmentHandler - Could not punish " + player.toString() + " (" + type.getName() + "): " + response.getErrorMessage());
        } else if (type == Punishment.PunishmentType.MUTE && (profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(player)).isPresent()) {
            Profile profile = profileOptional.get();
            profile.setMute(LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(response.asJSONObject().toString(), Punishment.class));
        }
        if (response.couldNotConnect()) {
            this.requestQueue.add(response.getRequestBuilder());
        }
        return response;
    }

    public RequestResponse pardon(UUID player, UUID sender, Punishment.PunishmentType type, String reason) {
        Optional<Profile> profileOptional;
        RequestResponse response;
        HashMap<String, Object> queryArgs = Maps.newHashMap();
        queryArgs.put("type", type.toString());
        queryArgs.put("reason", reason);
        if (Bukkit.getPlayer(sender) != null) {
            queryArgs.put("removedBy", sender.toString());
            queryArgs.put("removedByIp", Bukkit.getPlayer(sender).getAddress().getAddress().getHostAddress());
        }
        if (!(response = RequestHandler.delete("/users/" + player.toString() + "/activePunishment", queryArgs)).wasSuccessful()) {
            Bukkit.getLogger().warning("PunishmentHandler - Could not pardon " + player.toString() + " (" + type.getName() + "): " + response.getErrorMessage());
        } else if (type == Punishment.PunishmentType.MUTE && (profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(player)).isPresent()) {
            Profile profile = profileOptional.get();
            profile.setMute(null);
        }
        if (response.couldNotConnect()) {
            this.requestQueue.add(response.getRequestBuilder());
        }
        return response;
    }

    public void registerMetaFetcher(PunishmentMetaFetcher fetcher) {
        this.metaFetchers.add(fetcher);
    }

    private Map<String, Object> fixMeta(Map<String, Object> oldMap) {
        HashMap newMap = Maps.newHashMap();
        if (oldMap == null) {
            return newMap;
        }
        for (Map.Entry<String, Object> entry : oldMap.entrySet()) {
            newMap.put(entry.getKey(), this.normalise(entry.getValue()));
        }
        return newMap;
    }

    private Object normalise(Object otherObject) {
        if (otherObject instanceof Collection<?>) {
            ArrayList<Object> newList = Lists.newArrayList();
            for (Object object : (Collection<?>)otherObject) {
                newList.add(this.normalise(object));
            }
            return newList;
        }
        if (otherObject instanceof Map) {
            HashMap<Object, Object> newMap = Maps.newHashMap();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) otherObject).entrySet()) {
                newMap.put(this.normalise(entry.getKey()), this.normalise(entry.getValue()));
            }
            return newMap;
        }
        return String.valueOf(otherObject);
    }

}

