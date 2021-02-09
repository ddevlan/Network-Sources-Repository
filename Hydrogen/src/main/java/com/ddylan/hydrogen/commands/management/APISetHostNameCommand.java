/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package com.ddylan.hydrogen.commands.management;

import com.ddylan.hydrogen.Settings;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class APISetHostNameCommand {
    @Command(names={"api sethostname"}, permission="super", description="Set the API hostname")
    public static void apiSetHostName(CommandSender sender, @Param(name="hostname") String hostName) {
        Settings.setApiHost(hostName);
        Settings.save();
        sender.sendMessage((Object)ChatColor.GREEN + "API hostname set to \"" + (Object)ChatColor.WHITE + hostName + (Object)ChatColor.GREEN + "\"");
    }
}

