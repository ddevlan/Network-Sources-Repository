/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  com.ddylan.library.util.TimeUtils
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.listener.GeneralListener;
import com.ddylan.hydrogen.server.Server;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import com.ddylan.library.util.TimeUtil;
import mkremins.fanciful.FancyMessage;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class SeenCommand {
    @Command(names={"seen"}, permission="hydrogen.seen", description="See an user's IP and other info", async=true)
    public static void seen(CommandSender sender, @Param(name="player") PunishmentTarget target) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "An error occurred when contacting the Mojang API.");
                return;
            }
            Server server = Hydrogen.getInstance().getServerHandler().find(uuid).isPresent() ? Hydrogen.getInstance().getServerHandler().find(uuid).get() : null;
            //TODO: endpoint
            RequestResponse response = RequestHandler.get("/users/" + uuid.toString() + "/details");
            if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            JSONObject details = response.asJSONObject();
            JSONObject lastIpLog = null;
            if (details.has("ipLog")) {
                JSONArray array = details.getJSONArray("ipLog");
                for (Object logObject : array) {
                    JSONObject log = (JSONObject)logObject;
                    if (lastIpLog != null && log.getLong("lastSeenAt") <= lastIpLog.getLong("lastSeenAt")) continue;
                    lastIpLog = log;
                }
            }
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
            sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.YELLOW + ":");
            if (server != null) {
                long joinTime = GeneralListener.joinTime.get(uuid);
                int playSeconds = (int)((System.currentTimeMillis() - joinTime) / 1000L);
                sender.sendMessage(ChatColor.YELLOW + "Currently on " + ChatColor.RED + server.getDisplayName() + ChatColor.YELLOW + ". Online for " + ChatColor.RED + TimeUtil.formatIntoDetailedString((int)playSeconds) + (Object)ChatColor.YELLOW + ".");
            } else {
                Date lastSeen = new Date(details.getJSONObject("user").getLong("lastSeenAt"));
                sender.sendMessage(ChatColor.YELLOW + "Currently " + ChatColor.RED + "offline" + ChatColor.YELLOW + ". Last seen at " + ChatColor.RED + TimeUtil.formatIntoCalendarString((Date)lastSeen) + (Object)ChatColor.YELLOW + ".");
            }
            if (sender.hasPermission("hydrogen.seen.ip") && lastIpLog != null) {
                FancyMessage ipMessage = new FancyMessage("(Hover to show last known IP)").color(ChatColor.YELLOW);
                ipMessage.tooltip(ChatColor.RED + "sike bitch twitter.com/bizarreaiex nigga stay woketh :pray:");
                ipMessage.send(sender);
            }
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        });
    }
}

