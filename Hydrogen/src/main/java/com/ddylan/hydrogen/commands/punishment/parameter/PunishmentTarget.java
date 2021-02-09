/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.ParameterType
 *  com.ddylan.library.util.Callback
 *  com.ddylan.library.util.UUIDUtils
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.json.JSONObject
 *  org.json.JSONTokener
 */
package com.ddylan.hydrogen.commands.punishment.parameter;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.command.ParameterType;
import com.ddylan.library.util.callback.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class PunishmentTarget {
    private static final Pattern pattern = Pattern.compile("(?i)^[0-9a-f]{8}-?[0-9a-f]{4}-?[0-5][0-9a-f]{3}-?[089ab][0-9a-f]{3}-?[0-9a-f]{12}$");
    private static final List<String> recentlyDisconnected = new ArrayList<String>();
    private String name;

    private static final boolean isValidUuid(String uuid) {
        return uuid != null && uuid.trim().length() > 31 && pattern.matcher(uuid).matches();
    }

    public void resolveUUID(Callback<UUID> callback) {
        UUID uuid = LibraryPlugin.getInstance().getUuidCache().uuid(this.name);
        if (uuid != null) {
            callback.callback(uuid);
            return;
        }
        if (PunishmentTarget.isValidUuid(this.name)) {
            callback.callback(UUID.fromString(this.name));
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equalsIgnoreCase(this.name)) continue;
            this.name = player.getName();
            callback.callback(player.getUniqueId());
            return;
        }
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hydrogen.getInstance(), () -> {
            Request.Builder builder = new Request.Builder();
            builder.get();
            builder.url("https://api.mojang.com/users/profiles/minecraft/" + this.name);
            try {
                Response response = Hydrogen.getOkHttpClient().newCall(builder.build()).execute();
                String body = response.body().string();
                if (body.isEmpty()) {
                    callback.callback(null);
                    return;
                }
                JSONObject json = new JSONObject(new JSONTokener(body));
                this.name = json.getString("name");
                callback.callback(UUID.fromString(this.dash(json.getString("id"))));
            }
            catch (Exception e) {
                e.printStackTrace();
                callback.callback(null);
            }
        });
    }

    private String dash(String uuid) {
        StringBuffer sb = new StringBuffer(uuid);
        sb.insert(8, "-");
        sb = new StringBuffer(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuffer(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuffer(sb.toString());
        sb.insert(23, "-");
        return sb.toString();
    }

    public PunishmentTarget(String name) {
        this.name = name;
    }

    public static List<String> getRecentlyDisconnected() {
        return recentlyDisconnected;
    }

    public String getName() {
        return this.name;
    }

    public static class Type
    implements ParameterType<PunishmentTarget> {
        public PunishmentTarget transform(CommandSender sender, String source) {
            if (source.equals("self")) {
                source = sender.getName();
            }
            return new PunishmentTarget(source);
        }

        public List<String> tabComplete(Player sender, Set<String> flags, String source) {
            ArrayList<String> completions = new ArrayList<String>();
            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
            completions.addAll(recentlyDisconnected);
            return completions;
        }
    }
}

