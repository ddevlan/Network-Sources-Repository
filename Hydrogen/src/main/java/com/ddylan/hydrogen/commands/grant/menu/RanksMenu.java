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
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RanksMenu extends Menu {

    private static final String CREATE_GRANT_PERMISSION = "minehq.grant.create";
    private final String targetName;
    private final UUID targetUUID;

    public String getTitle(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Choose a Rank";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        List<Rank> ranks = this.getAllowedRanks(player);
        for (int i = 0; i < ranks.size(); ++i) {
            buttons.put(i, new RankButton(this.targetName, this.targetUUID, ranks.get(i)));
        }
        return buttons;
    }

    private List<Rank> getAllowedRanks(Player player) {
        List<Rank> allRanks = Hydrogen.getInstance().getRankHandler().getRanks();
        ArrayList ranks = Lists.newArrayList();
        for (int i = 0; i < allRanks.size(); ++i) {
            if (i == 0 || !this.isAllowed(allRanks.get(i), player)) continue;
            ranks.add(allRanks.get(i));
        }
        ranks.sort(Rank.DISPLAY_WEIGHT_COMPARATOR);
        return ranks;
    }

    private boolean isAllowed(Rank rank, Player player) {
        return player.hasPermission("minehq.grant.create." + rank.getId());
    }

    public void onClose(final Player player) {
        new BukkitRunnable(){

            public void run() {
                if (!Menu.currentlyOpenedMenus.containsKey(player.getName())) {
                    player.sendMessage(ChatColor.RED + "Granting cancelled.");
                }
            }
        }.runTaskLater(Hydrogen.getInstance(), 1L);
    }

    public RanksMenu(String targetName, UUID targetUUID) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
    }
}

