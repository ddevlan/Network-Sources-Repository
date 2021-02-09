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
package com.ddylan.hydrogen.commands.punishment.create;

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

public class WarnCommand {
    @Command(names={"warn"}, permission="minehq.punishment.create.warn", description="Add a warning to an user's account", async=true)
    public static void warn(CommandSender sender, @Param(name="target") PunishmentTarget target, @Param(name="reason", wildcard=true) String reason) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "Unable to locate UUID of " + target.getName() + ".");
                return;
            }
            UUID senderUUID = sender instanceof Player ? ((Player)sender).getUniqueId() : null;
            Player bukkitTarget = Bukkit.getPlayer(uuid);
            RequestResponse response = Hydrogen.getInstance().getPunishmentHandler().punish(uuid, senderUUID, Punishment.PunishmentType.WARN, reason, reason, -1L);
            if (response.couldNotConnect()) {
                sender.sendMessage(ChatColor.RED + "Could not reach API to complete punishment request. Adding a local punishment to the cache.");
                return;
            }
            if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            FancyMessage staffMessage = WarnCommand.getStaffMessage(sender, bukkitTarget, target.getName(), reason);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("minehq.punishment.create.warn")) continue;
                staffMessage.send(player);
            }
            staffMessage.send(Bukkit.getConsoleSender());
            bukkitTarget.sendMessage(new String[]{" ", " ", " "});
            bukkitTarget.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "You have been warned: " + ChatColor.YELLOW.toString() + ChatColor.BOLD + reason);
            bukkitTarget.sendMessage(new String[]{" ", " ", " "});
        });
    }

    private static FancyMessage getStaffMessage(CommandSender sender, Player bukkitTarget, String correctedTarget, String reason) {
        String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Console";
        return new FancyMessage(bukkitTarget == null ? ChatColor.GREEN + correctedTarget : bukkitTarget.getDisplayName()).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(" was ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then("warned by ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(senderName).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(".").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
    }
}

