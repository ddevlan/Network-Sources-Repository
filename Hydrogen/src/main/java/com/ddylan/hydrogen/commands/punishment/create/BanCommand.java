/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Flag
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.command.parameter.offlineplayer.OfflinePlayerWrapper
 *  com.ddylan.library.util.Callback
 *  com.ddylan.library.util.UUIDUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.ddylan.hydrogen.commands.punishment.create;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.Settings;
import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.punishment.Punishment;
import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Flag;
import com.ddylan.library.command.Param;
import com.ddylan.library.command.parameter.offlineplayer.OfflinePlayerWrapper;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BanCommand {
    public static final String KICK_MESSAGE = ChatColor.RED + "Your account has been suspended from the " + Settings.getNetworkName() + " Network. \n\nAppeal at " + Settings.getNetworkWebsite() + "/support";

    @Command(names={"ban", "b", "banish"}, permission="minehq.punishment.create.ban.permanent", description="Permanently banish an user from the network", async=true)
    public static void ban(CommandSender sender, @Flag(value={"s", "silent"}, description="Silently ban the player") boolean silent, @Flag(value={"c", "clear"}, description="Clear the player's inventory") boolean clear, @Param(name="target") PunishmentTarget target, @Param(name="reason", wildcard=true) String reason) {
        BanCommand.ban0(sender, target, reason, true, clear);
    }

    private static void ban0(CommandSender sender, PunishmentTarget target, String reason, boolean silent, boolean clear) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "Unable to locate UUID of " + target.getName() + ".");
                return;
            }
            UUID senderUUID = sender instanceof Player ? ((Player)sender).getUniqueId() : null;
            final Player bukkitTarget = Bukkit.getPlayer(uuid);
            RequestResponse response = Hydrogen.getInstance().getPunishmentHandler().punish(uuid, senderUUID, Punishment.PunishmentType.BAN, reason, reason, -1L);
            if (response.couldNotConnect()) {
                sender.sendMessage(ChatColor.RED + "Could not reach API to complete punishment request. Adding a local punishment to the cache.");
            } else if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            FancyMessage staffMessage = BanCommand.getStaffMessage(sender, bukkitTarget, target.getName(), reason, silent);
            if (!silent) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (player2.hasPermission("minehq.punishment.view")) {
                        staffMessage.send(player2);
                        continue;
                    }
                    player2.sendMessage(ChatColor.GREEN + target.getName() + " was banned by " + sender.getName() + ".");
                }
            } else {
                for (Player player3 : Bukkit.getOnlinePlayers()) {
                    if (!player3.hasPermission("minehq.punishment.view")) continue;
                    staffMessage.send(player3);
                }
            }
            staffMessage.send(Bukkit.getConsoleSender());
            if (clear) {
                OfflinePlayerWrapper wrapper = new OfflinePlayerWrapper(LibraryPlugin.getInstance().getUuidCache().name(uuid));
                wrapper.loadAsync(player -> Bukkit.getScheduler().runTask(Hydrogen.getInstance(), () -> {
                    player.getInventory().clear();
                    Bukkit.getScheduler().runTaskAsynchronously(Hydrogen.getInstance(), ((Player)player)::saveData);
                }));
            }
            if (bukkitTarget != null) {
                new BukkitRunnable(){

                    public void run() {
                        bukkitTarget.kickPlayer(KICK_MESSAGE);
                    }
                }.runTaskLater(Hydrogen.getInstance(), 1L);
            }
        });
    }

    private static FancyMessage getStaffMessage(CommandSender sender, Player bukkitTarget, String correctedTarget, String reason, boolean silent) {
        String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Console";
        FancyMessage staffMessage = new FancyMessage(bukkitTarget == null ? ChatColor.GREEN + correctedTarget : bukkitTarget.getDisplayName()).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(" was ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        if (silent) {
            staffMessage.then("silently ").color(ChatColor.YELLOW).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        }
        staffMessage.then("banned by ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(senderName).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(".").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        return staffMessage;
    }
}

