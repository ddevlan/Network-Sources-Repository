/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.commands.auth;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ForceAuthCommand {
    @Command(names={"forceauth"}, permission="op", description="Forcefully authenticate a player")
    public static void forceAuth(CommandSender sender, @Param(name="player") Player player) {
        player.removeMetadata("Locked", Hydrogen.getInstance());
        player.setMetadata("ForceAuth", new FixedMetadataValue(Hydrogen.getInstance(), true));
        sender.sendMessage(ChatColor.YELLOW + player.getName() + " has been forcefully authenticated.");
    }
}

