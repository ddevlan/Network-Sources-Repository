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
package com.ddylan.hydrogen.commands.auth;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.Optional;

public class AuthCommand {

    @Command(names={"auth", "authenticate", "2fa", "otp"}, permission="", description="Authenticate with the API, verifying your identity")
    public static void auth(Player sender, @Param(name="code", wildcard=true) String input) {
        if (!sender.hasMetadata("Locked")) {
            sender.sendMessage(ChatColor.RED + "You don't need to authenticate at the moment.");
            return;
        }
        input = input.replace(" ", "");
        int code = Integer.parseInt(input);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Hydrogen.getInstance(), () -> {
            RequestResponse response = RequestHandler.post("/users/" + sender.getUniqueId().toString() + "/verifyTotp", ImmutableMap.of("totpCode", code, "userIp", sender.getAddress().getAddress().getHostAddress()));
            if (response.wasSuccessful()) {
                Bukkit.getScheduler().runTask(Hydrogen.getInstance(), () -> {
                    JSONObject object = response.asJSONObject();
                    boolean authorized = object.getBoolean("authorized");
                    if (!authorized) {
                        sender.sendMessage(ChatColor.RED + object.getString("message"));
                        return;
                    }
                    Optional<Profile> profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(sender.getUniqueId());
                    if (!profileOptional.isPresent()) {
                        sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Your profile hasn't yet been loaded.");
                        return;
                    }
                    Profile profile = profileOptional.get();
                    profile.authenticated();
                    profile.updatePlayer(sender);
                    sender.sendMessage(ChatColor.GREEN + "Your identity has been verified.");
                });
            } else {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                Bukkit.getLogger().warning("TOTP - Failed to authenticate " + sender.getUniqueId() + ": " + response.getErrorMessage());
            }
        });
    }

}

