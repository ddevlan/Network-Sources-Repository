/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.library.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class ListCommand {

    @Command(names={"list", "who", "players"}, permission="hydrogen.list", description="See a list of online players")
    public static void list(CommandSender sender) {
        TreeMap<Rank, LinkedList<String>> sorted = new TreeMap<>(Rank.DISPLAY_WEIGHT_COMPARATOR);
        int online = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            ++online;
            if (sender instanceof Player && !((Player)sender).canSee(player) && player.hasMetadata("invisible")) continue;
            Optional<Profile> profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(player.getUniqueId());
            Rank rank = null;
            if (profileOptional.isPresent()) {
                Profile profile = profileOptional.get();
                rank = profile.getBestDisplayRank();
            }
            Rank defaultRank = Hydrogen.getInstance().getRankHandler().getRank("default").orElse(null);
            if (rank == null || defaultRank != null && player.isDisguised()) {
                rank = defaultRank;
            }
            String displayName = player.getDisplayName();
            if (player.hasMetadata("invisible")) {
                displayName = ChatColor.GRAY + "*" + displayName;
            }
            sorted.putIfAbsent(rank, new LinkedList<>());
            ((List<String>)sorted.get(rank)).add(displayName);
        }
        LinkedList<String> merged = new LinkedList<>();
        for (List<String> part : sorted.values()) {
            part.sort(String.CASE_INSENSITIVE_ORDER);
            merged.addAll(part);
        }
        sender.sendMessage(ListCommand.getHeader());
        sender.sendMessage("(" + online + "/" + Bukkit.getMaxPlayers() + ") " + merged);
    }

    private static String getHeader() {
        StringBuilder builder = new StringBuilder();
        for (Rank rank : Hydrogen.getInstance().getRankHandler().getRanks()) {
            boolean displayed = rank.getDisplayWeight() > 0;
            if (!displayed) continue;
            builder.append(rank.getFormattedName()).append(ChatColor.RESET).append(", ");
        }
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }
}

