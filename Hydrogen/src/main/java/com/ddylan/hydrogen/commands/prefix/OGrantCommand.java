/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  com.ddylan.library.util.TimeUtils
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.commands.prefix;

import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import com.ddylan.library.util.TimeUtil;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class OGrantCommand {
    @Command(names={"oprefixgrant", "ograntprefix"}, permission="hydrogen.grantprefix", description="Add a grant to an user's account", async=true)
    public static void grant(CommandSender sender, @Param(name="player") PunishmentTarget target, @Param(name="prefix") String prefix, @Param(name="duration") String timeString, @Param(name="scopes") String scopes, @Param(name="reason", wildcard=true) String reason) {
        int duration = -1;
        if (!StringUtils.startsWithIgnoreCase(timeString, "perm") && (duration = Math.toIntExact(TimeUtil.parseTime(timeString) + 1)) <= 0) {
            sender.sendMessage(ChatColor.RED + "'" + timeString + "' is not a valid duration.");
            return;
        }
        if (scopes.equalsIgnoreCase("global")) {
            scopes = "";
        }
        String finalScopes = scopes;
        int expiresIn = duration;
        if (!sender.hasPermission("minehq.prefixgrant.create." + prefix.toLowerCase())) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
            return;
        }
        target.resolveUUID(uuid -> {
            RequestResponse response;
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "An error occurred when contacting the Mojang API.");
                return;
            }
            HashMap<String, Object> body = new HashMap<>();
            body.put("user", uuid);
            body.put("reason", reason);
            body.put("scopes", finalScopes.isEmpty() ? ImmutableList.of() : finalScopes.split(","));
            body.put("prefix", prefix);
            if (expiresIn > 0) {
                body.put("expiresIn", expiresIn);
            }
            if (sender instanceof Player) {
                body.put("addedBy", ((Player)sender).getUniqueId().toString());
                body.put("addedByIp", ((Player)sender).getAddress().getAddress().getHostAddress());
            }
            if ((response = RequestHandler.post("/prefixes", body)).wasSuccessful()) {
                sender.sendMessage(ChatColor.GREEN + "Successfully granted " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " the " + ChatColor.WHITE + prefix + ChatColor.GREEN + " prefix.");
            } else {
                sender.sendMessage(ChatColor.RED + response.getErrorMessage());
            }
        });
    }
}

