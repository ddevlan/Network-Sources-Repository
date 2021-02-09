/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.ddylan.library.menu.Button
 *  com.ddylan.library.menu.Menu
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.ddylan.hydrogen.commands.grant.menu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.hydrogen.server.ServerGroup;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import com.google.common.collect.Maps;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScopesMenu extends Menu {

    private final Map<String, Boolean> status = Maps.newHashMap();
    private boolean global = false;
    private boolean complete;
    private Rank rank;
    private String targetName;
    private UUID targetUUID;
    private String reason;
    private int duration;

    public String getTitle(Player player) {
        return ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + "Select the Scopes";
    }

    public Map<Integer, Button> getButtons(Player player) {
        val buttons = new HashMap<Integer, Button>();
        val groups = new ArrayList<ServerGroup>();
        groups.addAll(Hydrogen.getInstance().getServerHandler().getServerGroups());
        groups.sort((first, second) -> first.getId().compareToIgnoreCase(second.getId()));
        int i = 0;
        for (ServerGroup scope : groups) {
            this.status.putIfAbsent(scope.getId(), false);
            buttons.put(i, new ScopeButton(this, scope));
            ++i;
        }
        val scopes = new ArrayList<ServerGroup>();
        scopes.addAll(this.status.keySet().stream().filter(this.status::get).map(key -> Hydrogen.getInstance().getServerHandler().getServerGroup(key).orElse(null)).collect(Collectors.toList()));
        buttons.put(22, new GlobalButton(this));
        buttons.put(31, new GrantButton(this.rank, this.targetName, this.targetUUID, this.reason, this, scopes, this.duration));
        return buttons;
    }

    public void onClose(final Player player) {
        new BukkitRunnable(){

            public void run() {
                if (!Menu.currentlyOpenedMenus.containsKey(player.getName()) && !ScopesMenu.this.complete) {
                    player.sendMessage((Object)ChatColor.RED + "Granting cancelled.");
                }
            }
        }.runTaskLater(Hydrogen.getInstance(), 1L);
    }

    public ScopesMenu(boolean global, boolean complete, Rank rank, String targetName, UUID targetUUID, String reason, int duration) {
        this.global = global;
        this.complete = complete;
        this.rank = rank;
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.reason = reason;
        this.duration = duration;
    }

    public Map<String, Boolean> getStatus() {
        return this.status;
    }

    public boolean isGlobal() {
        return this.global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}

