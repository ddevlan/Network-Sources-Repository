/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  com.ddylan.library.util.TimeUtils
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
import com.ddylan.library.util.TimeUtil;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MuteCommand {
    @Command(names={"mute"}, permission="minehq.punishment.create.mute", description="Temporarily mute an user, stopping them from talking in public chat", async=true)
    public static void mute(CommandSender sender, @Param(name="target") PunishmentTarget target, @Param(name="time") String timeString, @Param(name="reason", wildcard=true) String reason) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "Unable to locate UUID of " + target.getName() + ".");
                return;
            }
            UUID senderUUID = sender instanceof Player ? ((Player)sender).getUniqueId() : null;
            Player bukkitTarget = Bukkit.getPlayer(uuid);
            int seconds = Math.toIntExact(TimeUtil.parseTime(timeString));
            if (senderUUID != null && !sender.hasPermission("minehq.punishment.create.mute.permanent") && TimeUnit.DAYS.toSeconds(31L) < (long)seconds) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to create a mute this long. Maximum time allowed: 30 days.");
                return;
            }
            RequestResponse response = Hydrogen.getInstance().getPunishmentHandler().punish(uuid, senderUUID, Punishment.PunishmentType.MUTE, reason, reason, seconds);
            if (response.couldNotConnect()) {
                sender.sendMessage(ChatColor.RED + "Could not reach API to complete punishment request. Adding a local punishment to the cache.");
                return;
            }
            if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            FancyMessage staffMessage = MuteCommand.getStaffMessage(sender, bukkitTarget, target.getName(), seconds, reason);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("minehq.punishment.create.mute")) continue;
                staffMessage.send(player);
            }
            staffMessage.send(Bukkit.getConsoleSender());
            if (bukkitTarget != null) {
                bukkitTarget.sendMessage(ChatColor.RED + "You have been silenced for " + TimeUtil.formatIntoDetailedString(seconds) + ".");
            }
        });
    }

    public static FancyMessage getStaffMessage(CommandSender sender, Player bukkitTarget, String correctedTarget, int seconds, String reason) {
        String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Console";
        return new FancyMessage(bukkitTarget == null ? ChatColor.GREEN + correctedTarget : bukkitTarget.getDisplayName()).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason, ChatColor.YELLOW + "Duration: " + ChatColor.RED + TimeUtil.formatIntoDetailedString(seconds)).then(" was temporarily muted by ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason, ChatColor.YELLOW + "Duration: " + ChatColor.RED + TimeUtil.formatIntoDetailedString(seconds)).then(senderName).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason, ChatColor.YELLOW + "Duration: " + ChatColor.RED + TimeUtil.formatIntoDetailedString(seconds)).then(".").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason, ChatColor.YELLOW + "Duration: " + ChatColor.RED + TimeUtil.formatIntoDetailedString(seconds));
    }
}

