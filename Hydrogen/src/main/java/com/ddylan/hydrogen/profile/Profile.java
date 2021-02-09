/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.ddylan.library.nametag.FrozenNametagHandler
 *  com.ddylan.library.qLib
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.profile;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.Settings;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.prefix.Prefix;
import com.ddylan.hydrogen.prefix.PrefixHandler;
import com.ddylan.hydrogen.punishment.Punishment;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.hydrogen.rank.RankHandler;
import com.ddylan.hydrogen.util.PermissionUtils;
import com.ddylan.library.LibraryPlugin;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public final class Profile {
    private UUID player;
    private List<Rank> ranks;
    private Map<String, List<Rank>> scopeRanks;
    private boolean accessAllowed;
    private String accessDenialReason;
    private Map<String, Boolean> permissions;
    private boolean totpRequired;
    private boolean authenticated;
    private boolean ipWhitelisted;
    private Punishment mute;
    private ChatColor iconColor;
    private ChatColor nameColor;
    private Set<Prefix> authorizedPrefixes;
    private Prefix activePrefix;
    private Rank bestGeneralRank;
    private Rank bestDisplayRank;

    public Profile(UUID uuid, JSONObject json) {
        this.update(uuid, json);
    }

    public void update(UUID uuid, JSONObject json) {
        RankHandler rankHandler = Hydrogen.getInstance().getRankHandler();
        PrefixHandler prefixHandler = Hydrogen.getInstance().getPrefixHandler();
        this.player = uuid;
        JSONArray ranks = json.getJSONArray("ranks");
        this.ranks = new ArrayList<>();
        for (int i = 0; i < ranks.length(); ++i) {
            Optional<Rank> rank = rankHandler.getRank(ranks.getString(i));
            rank.ifPresent(this.ranks::add);
        }
        Rank bestGeneralRank = null;
        for (Rank rank : this.ranks) {
            if (bestGeneralRank != null && rank.getGeneralWeight() < bestGeneralRank.getGeneralWeight()) continue;
            bestGeneralRank = rank;
        }
        this.bestGeneralRank = bestGeneralRank;
        Rank bestDisplayRank = null;
        for (Rank rank : this.ranks) {
            if (bestDisplayRank != null && rank.getDisplayWeight() < bestDisplayRank.getDisplayWeight()) continue;
            bestDisplayRank = rank;
        }
        this.bestDisplayRank = bestDisplayRank;
        this.scopeRanks = new HashMap<>();

        if (json.has("scopeRanks")) {
            json.getJSONObject("scopeRanks").toMap().forEach((scope, rawRanks) -> {
                List<Rank> parsedRanks = new ArrayList<>();
                ArrayList<String> rawRank$ = (ArrayList<String>) rawRanks;
                for (String rawRank : rawRank$) {
                    Optional<Rank> parsedRank = rankHandler.getRank(rawRank);

                    parsedRank.ifPresent(parsedRanks::add);
                }

                this.scopeRanks.put(scope, parsedRanks);
            });
        }

        this.mute = json.has("mute") ? LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(json.get("mute").toString(), Punishment.class) : null;
        if (json.has("access")) {
            this.accessAllowed = json.getJSONObject("access").optBoolean("allowed", true);
            this.accessDenialReason = json.getJSONObject("access").optString("message", "");
        } else {
            this.accessAllowed = true;
            this.accessDenialReason = "";
        }
        HashMap hashMap = Maps.newHashMap();
        for (Rank rank : this.ranks) {
            Map<String, Boolean> map = PermissionUtils.mergePermissions(permissions, Hydrogen.getInstance().getPermissionHandler().getPermissions(rank));
        }
        this.iconColor = json.has("iconColor") ? ChatColor.valueOf(json.getString("iconColor")) : null;
        this.nameColor = json.has("nameColor") ? ChatColor.valueOf(json.getString("nameColor")) : null;
        if (json.has("prefixes")) {
            JSONArray jSONArray = json.getJSONArray("prefixes");
            HashSet authorizedPrefixes = Sets.newHashSet();
            for (int i = 0; i < jSONArray.length(); ++i) {
                Optional<Prefix> prefix2 = prefixHandler.getPrefix(jSONArray.getString(i));
                prefix2.ifPresent(authorizedPrefixes::add);
            }
            this.authorizedPrefixes = authorizedPrefixes;
        } else {
            this.authorizedPrefixes = ImmutableSet.of();
        }
        if (json.has("activePrefix")) {
            prefixHandler.getPrefix(json.getString("activePrefix")).ifPresent(prefix -> {
                if (this.authorizedPrefixes.stream().anyMatch(ap -> ap.getId().equals(prefix.getId()))) {
                    this.activePrefix = prefix;
                }
            });
        }
    }

    public void checkTotpLock(String ip) {
        ProfileHandler profileHandler = Hydrogen.getInstance().getProfileHandler();
        if (!profileHandler.getTotpEnabled().contains(this.player) && !this.getBestGeneralRank().isStaffRank()) {
            this.totpRequired = false;
            return;
        }
        //TODO: endpoint
        RequestResponse response = RequestHandler.get("/users/" + this.player.toString() + "/requiresTotp", (ImmutableMap.of("userIp", ip)));
        if (response.wasSuccessful()) {
            this.totpRequired = response.asJSONObject().getBoolean("required");
            this.ipWhitelisted = response.asJSONObject().getString("message").equals("NOT_REQUIRED_IP_PRE_AUTHORIZED");
        }
    }

    private void updatePermissions(Player player) {
        Hydrogen.getInstance().getPermissionHandler().update(player, this.permissions);
    }

    public void updatePlayer(final Player player) {
        if (!this.accessAllowed) {
            new BukkitRunnable(){

                public void run() {
                    if (Profile.this.accessDenialReason != null && Profile.this.accessDenialReason.contains("VPNs are not allowed")) {
                        player.kickPlayer(Profile.this.accessDenialReason);
                        return;
                    }
                    if (Settings.isBannedJoinsEnabled()) {
                        String message = ChatColor.RED + "Banned players are only allowed to use /register.";
                        player.setMetadata("Locked", new FixedMetadataValue(Hydrogen.getInstance(), message));
                    } else {
                        player.kickPlayer(Profile.this.accessDenialReason);
                    }
                }
            }.runTask(Hydrogen.getInstance());
            return;
        }
        Rank bestDisplayRank = this.getBestDisplayRank();
        String gameColor = bestDisplayRank.getGameColor();
        if (bestDisplayRank.getId().equals("velt-plus")) {
            gameColor = Objects.firstNonNull(this.nameColor, (Object)ChatColor.WHITE).toString() + ChatColor.BOLD;
        }
        player.setDisplayName(gameColor + player.getName() + ChatColor.RESET);
        String gamePrefix = bestDisplayRank.getGamePrefix();
        if (bestDisplayRank.getId().equals("velt-plus")) {
            gamePrefix = Objects.firstNonNull(this.iconColor, (Object)ChatColor.GRAY).toString() + "\u2738" + gameColor + ChatColor.BOLD;
        }
        if (this.activePrefix != null && !Settings.isClean()) {
            gamePrefix = gamePrefix + ChatColor.translateAlternateColorCodes('&', this.activePrefix.getPrefix());
        }
        player.setMetadata("HydrogenPrefix", new FixedMetadataValue(Hydrogen.getInstance(), gamePrefix));
        LibraryPlugin.getInstance().getNametagHandler().reloadPlayer(player);
        boolean totpRequired = false;
        boolean userTotpRequired = this.totpRequired;
        boolean staffTotpRequired = Settings.isForceStaffTotp() && this.getBestGeneralRank().isStaffRank();
        String totpMessage = null;
        if ((userTotpRequired || staffTotpRequired) && !this.authenticated && !this.ipWhitelisted) {
            totpRequired = true;
        }
        if (totpRequired && !player.hasMetadata("ForceAuth")) {
            totpMessage = !userTotpRequired && staffTotpRequired ? ChatColor.RED + "Please set up your two-factor authentication using /2fasetup." : ChatColor.RED + "Please provide your two-factor code. Type \"/auth <code>\" to authenticate.";
            player.setMetadata("Locked", new FixedMetadataValue(Hydrogen.getInstance(), totpMessage));
        } else {
            player.removeMetadata("Locked", Hydrogen.getInstance());
        }
        this.updatePermissions(player);
        final String finalTotpMessage = totpMessage;
        new BukkitRunnable(){

            public void run() {
                if (finalTotpMessage != null) {
                    player.sendMessage(finalTotpMessage);
                }
            }
        }.runTaskLater(Hydrogen.getInstance(), 10L);
    }

    public void setActivePrefix(Prefix prefix) {
        this.activePrefix = prefix;
        Bukkit.getScheduler().runTaskAsynchronously(Hydrogen.getInstance(), () -> {
            HashMap<String, Object> meta = Maps.newHashMap();
            meta.put("prefix", prefix == null ? null : prefix.getId());
            RequestResponse response = RequestHandler.post("/users/" + this.player + "/prefix", meta);
            if (!response.wasSuccessful()) {
                Bukkit.getLogger().info(response.asJSONObject().toString());
            }
        });
    }

    public void authenticated() {
        this.authenticated = true;
    }

    public boolean isMuted() {
        return this.mute != null;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public List<Rank> getRanks() {
        return this.ranks;
    }

    public Map<String, List<Rank>> getScopeRanks() {
        return this.scopeRanks;
    }

    public boolean isAccessAllowed() {
        return this.accessAllowed;
    }

    public String getAccessDenialReason() {
        return this.accessDenialReason;
    }

    public Map<String, Boolean> getPermissions() {
        return this.permissions;
    }

    public boolean isTotpRequired() {
        return this.totpRequired;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public boolean isIpWhitelisted() {
        return this.ipWhitelisted;
    }

    public Punishment getMute() {
        return this.mute;
    }

    public void setMute(Punishment mute) {
        this.mute = mute;
    }

    public ChatColor getIconColor() {
        return this.iconColor;
    }

    public void setIconColor(ChatColor iconColor) {
        this.iconColor = iconColor;
    }

    public ChatColor getNameColor() {
        return this.nameColor;
    }

    public void setNameColor(ChatColor nameColor) {
        this.nameColor = nameColor;
    }

    public Set<Prefix> getAuthorizedPrefixes() {
        return this.authorizedPrefixes;
    }

    public void setAuthorizedPrefixes(Set<Prefix> authorizedPrefixes) {
        this.authorizedPrefixes = authorizedPrefixes;
    }

    public Prefix getActivePrefix() {
        return this.activePrefix;
    }

    public Rank getBestGeneralRank() {
        return this.bestGeneralRank;
    }

    public Rank getBestDisplayRank() {
        return this.bestDisplayRank;
    }
}

