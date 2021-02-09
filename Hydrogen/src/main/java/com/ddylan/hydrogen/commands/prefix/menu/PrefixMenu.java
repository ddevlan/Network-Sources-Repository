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
package com.ddylan.hydrogen.commands.prefix.menu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.prefix.Prefix;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PrefixMenu extends Menu {

    private static final String CREATE_GRANT_PERMISSION = "minehq.prefixgrant.create";
    private String targetName;
    private UUID targetUUID;

    public String getTitle(Player player) {
        return ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + "Choose a Prefix";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        List<Prefix> prefixes = this.getAllowedPrefixes(player);
        for (int i = 0; i < prefixes.size(); ++i) {
            buttons.put(i, new PrefixButton(this.targetName, this.targetUUID, prefixes.get(i)));
        }
        return buttons;
    }

    private List<Prefix> getAllowedPrefixes(Player player) {
        List<Prefix> allPrefixes = Hydrogen.getInstance().getPrefixHandler().getPrefixes();
        ArrayList prefixes = Lists.newArrayList();
        for (int i = 0; i < allPrefixes.size(); ++i) {
            if (!this.isAllowed(allPrefixes.get(i), player)) continue;
            prefixes.add(allPrefixes.get(i));
        }
        return prefixes;
    }

    private boolean isAllowed(Prefix prefix, Player player) {
        return player.hasPermission("minehq.prefixgrant.create." + prefix.getId());
    }

    public void onClose(final Player player) {
        new BukkitRunnable(){

            public void run() {
                if (!Menu.currentlyOpenedMenus.containsKey(player.getName())) {
                    player.sendMessage((Object)ChatColor.RED + "Granting cancelled.");
                }
            }
        }.runTaskLater((Plugin) Hydrogen.getInstance(), 1L);
    }

    public PrefixMenu(String targetName, UUID targetUUID) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
    }
}

