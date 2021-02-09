/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.commands.punishment.remove;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.punishment.Punishment;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnBanCommand {
    @Command(names={"unban"}, permission="minehq.punishment.remove.ban", description="Lift an user's ban", async=true)
    public static void unban(CommandSender sender, @Param(name="target") PunishmentTarget target, @Param(name="reason", wildcard=true) String reason) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "An error occurred when contacting the Mojang API.");
                return;
            }
            UUID senderUUID = sender instanceof Player ? ((Player)sender).getUniqueId() : null;
            Player bukkitTarget = Bukkit.getPlayer(uuid);
            RequestResponse response = Hydrogen.getInstance().getPunishmentHandler().pardon(uuid, senderUUID, Punishment.PunishmentType.BAN, reason);
            if (response.couldNotConnect()) {
                sender.sendMessage(ChatColor.RED + "Could not reach API to complete pardon request. Adding request to queue.");
                return;
            }
            if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            FancyMessage staffMessage = UnBanCommand.getStaffMessage(sender, bukkitTarget, target.getName(), reason);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("minehq.punishment.create.ban")) continue;
                staffMessage.send(player);
            }
            staffMessage.send(Bukkit.getConsoleSender());
        });
    }

    private static FancyMessage getStaffMessage(CommandSender sender, Player bukkitTarget, String correctedTarget, String reason) {
        String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Console";
        FancyMessage staffMessage = new FancyMessage(bukkitTarget == null ? ChatColor.GREEN + correctedTarget : bukkitTarget.getDisplayName()).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(" was ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        staffMessage.then("unbanned by ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(senderName).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(".").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        return staffMessage;
    }
}

