/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.qLib
 *  com.ddylan.library.redis.RedisCommand
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  redis.clients.jedis.Jedis
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MotdCommand {
    private static String line1;
    private static String line2;

    @Command(names={"motd set"}, permission="super")
    public static void motdSet(CommandSender sender, @Param(name="line") int line, @Param(name="text", wildcard=true) String text) {
        if (line == 1) {
            line1 = text;
        } else if (line == 2) {
            line2 = text;
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid line number...");
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Line " + line + " set to \"" + text + "\"");
    }

    @Command(names={"motd push"}, permission="super", async=true)
    public static void motdPush(final CommandSender sender) {
        if (line1 == null) {
            sender.sendMessage(ChatColor.RED + "Line 1 is not set! Unable to push.");
        } else if (line2 == null) {
            sender.sendMessage(ChatColor.RED + "Line 2 is not set! Unable to push.");
        } else {
            LibraryPlugin.getInstance().getXJedis().runBackboneRedisCommand(jedis -> {
                jedis.set("BungeeCordMOTD", line1 + "\n" + line2);
                sender.sendMessage(ChatColor.GREEN + "MOTD set.");
                return null;
            });
        }
    }
}

