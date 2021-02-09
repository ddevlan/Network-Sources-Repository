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

import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class APISetKeyCommand {
    @Command(names={"api setkey"}, permission="super", description="Set the API key we use to authenticate")
    public static void apiSetKey(CommandSender sender, @Param(name="key") String key) {
        sender.sendMessage(ChatColor.RED + "This command is no longer in use. Please configure the server-name in server.properties.");
    }
}

