/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.ddylan.library.command.Command
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.Settings;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.command.Command;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class LoginCommand {

    @Command(names={"login"}, permission="", description="Generate a disposable token which can be used to log into the website")
    public static void login(Player sender) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hydrogen.getInstance(), () -> {
            RequestResponse response = RequestHandler.post("/disposableLoginTokens", ImmutableMap.of("user", sender.getUniqueId(), "userIp", sender.getAddress().getAddress().getHostAddress()));
            if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            JSONObject json = response.asJSONObject();
            if (json.getBoolean("success")) {
                sender.sendMessage(ChatColor.GREEN + "www." + Settings.getNetworkWebsite() + "/login/" + json.getString("token"));
                sender.sendMessage(ChatColor.YELLOW + "The link above expires in 5 minutes.");
            }
        });
    }
}

