/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.util.TimeUtils
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package com.ddylan.hydrogen.commands.management;

import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.library.command.Command;
import com.ddylan.library.util.TimeUtil;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class APICommand {
    @Command(names={"api"}, permission="hydrogen.debug", description="See API info")
    public static void api(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        if (sender.hasPermission("super")) {
            // empty if block
        }
        sender.sendMessage(ChatColor.YELLOW + "Status: " + (RequestHandler.isApiDown() ? ChatColor.RED + "Offline" : ChatColor.GREEN + "Online"));
        sender.sendMessage(ChatColor.YELLOW + "Last Request: " + (RequestHandler.getLastAPIRequest() == 0L ? ChatColor.GREEN + "Never :3" : (Object)ChatColor.RED + TimeUtil.formatIntoDetailedString((int)((int)(System.currentTimeMillis() - RequestHandler.getLastAPIRequest()) / 1000)) + " ago"));
        sender.sendMessage(ChatColor.YELLOW + "Last Error: " + (RequestHandler.getLastAPIError() == 0L ? ChatColor.GREEN + "Never :3" : (Object)ChatColor.RED + TimeUtil.formatIntoDetailedString((int)((int)(System.currentTimeMillis() - RequestHandler.getLastAPIError()) / 1000)) + " ago"));
        sender.sendMessage(ChatColor.YELLOW + "Last Latency: " + ChatColor.RED + RequestHandler.getLastLatency() + "ms");
        sender.sendMessage(ChatColor.YELLOW + "Average Latency: " + ChatColor.RED + RequestHandler.getAverageLatency() + "ms");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
    }
}

