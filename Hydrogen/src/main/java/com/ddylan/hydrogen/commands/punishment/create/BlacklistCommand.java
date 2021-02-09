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

public class BlacklistCommand {
    public static final String KICK_MESSAGE = ChatColor.RED + "Your account has been blacklisted from the " + Settings.getNetworkName() + " Network. \n\nThis type of punishment cannot be appealed.";

    @Command(names={"blacklist", "bl"}, permission="minehq.punishment.create.blacklist", description="Blacklist an user from the network. This type of punishment cannot be appealed", async=true)
    public static void blacklist(CommandSender sender, @Flag(value={"s", "silent"}, description="Silently blacklist the player") boolean silent, @Flag(value={"c", "clear"}, description="Clear the player's inventory") boolean clear, @Param(name="player") PunishmentTarget target, @Param(name="reason", wildcard=true) String reason) {
        BlacklistCommand.blacklist0(sender, target, reason, true, clear);
    }

    private static void blacklist0(CommandSender sender, PunishmentTarget target, String reason, boolean silent, boolean clear) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "Unable to locate UUID of " + target.getName() + ".");
                return;
            }
            UUID senderUUID = sender instanceof Player ? ((Player)sender).getUniqueId() : null;
            final Player bukkitTarget = Bukkit.getPlayer(uuid);
            RequestResponse response = Hydrogen.getInstance().getPunishmentHandler().punish(uuid, senderUUID, Punishment.PunishmentType.BLACKLIST, reason, reason, -1L);
            if (response.couldNotConnect()) {
                sender.sendMessage(ChatColor.RED + "Could not reach API to complete punishment request. Adding a local punishment to the cache.");
            } else if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            FancyMessage staffMessage = BlacklistCommand.getStaffMessage(sender, bukkitTarget, target.getName(), reason, silent);
            if (!silent) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (player2.hasPermission("minehq.punishment.create.blacklist")) {
                        staffMessage.send(player2);
                        continue;
                    }
                    player2.sendMessage(ChatColor.GREEN + target.getName() + " was blacklisted by " + sender.getName() + ".");
                }
            } else {
                for (Player player3 : Bukkit.getOnlinePlayers()) {
                    if (!player3.hasPermission("minehq.punishment.create.blacklist")) continue;
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
                }.runTask(Hydrogen.getInstance());
            }
        });
    }

    private static FancyMessage getStaffMessage(CommandSender sender, Player bukkitTarget, String correctedTarget, String reason, boolean silent) {
        String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Console";
        FancyMessage staffMessage = new FancyMessage(bukkitTarget == null ? ChatColor.GREEN + correctedTarget : bukkitTarget.getDisplayName()).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(" was ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        if (silent) {
            staffMessage.then("silently ").color(ChatColor.YELLOW).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        }
        staffMessage.then("blacklisted by ").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(senderName).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason).then(".").color(ChatColor.GREEN).tooltip(ChatColor.YELLOW + "Reason: " + ChatColor.RED + reason);
        return staffMessage;
    }
}

