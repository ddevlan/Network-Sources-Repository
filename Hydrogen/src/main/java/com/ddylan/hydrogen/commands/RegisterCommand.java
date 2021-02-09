/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.json.JSONObject
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class RegisterCommand {
    @Command(names={"register"}, permission="", description="Become a member of the network!")
    public static void register(Player sender, @Param(name="email") String email) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hydrogen.getInstance(), () -> {
            RequestResponse response = RequestHandler.post("/users/" + sender.getUniqueId() + "/registerEmail",ImmutableMap.of("email", email, "userIp", sender.getAddress().getAddress().getHostAddress()));
            if (!response.wasSuccessful()) {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                return;
            }
            JSONObject json = response.asJSONObject();
            if (json.getBoolean("success")) {
                sender.sendMessage(ChatColor.GREEN + "You should be receiving a confirmation e-mail shortly.");
            }
        });
    }
}

