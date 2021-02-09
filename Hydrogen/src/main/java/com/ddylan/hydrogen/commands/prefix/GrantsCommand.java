/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.qLib
 *  com.ddylan.library.util.Callback
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.commands.prefix;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.prefix.PrefixGrant;
import com.ddylan.hydrogen.prefix.menu.PrefixGrantMenu;
import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;

public class GrantsCommand {

    @Command(names={"prefixgrants"}, permission="hydrogen.grantprefix", description="Check a user's grants")
    public static void grants(Player sender, @Param(name="target") PunishmentTarget target) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "An error occurred when contacting the Mojang API.");
                return;
            }
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Hydrogen.getInstance(), () -> {
                //TODO: endpoint
                RequestResponse response = RequestHandler.get("/prefixes/grants", ImmutableMap.of("user", uuid));
                if (response.wasSuccessful()) {
                    List<PrefixGrant> allGrants = LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<List<PrefixGrant>>(){}.getType());
                    allGrants.sort((first, second) -> {
                        if (first.getAddedAt() > second.getAddedAt()) {
                            return -1;
                        }
                        return 1;
                    });
                    LinkedHashMap<PrefixGrant, String> grants = new LinkedHashMap<>();
                    allGrants.forEach(grant -> grants.put(grant, grant.resolveAddedBy()));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Hydrogen.getInstance(), () -> new PrefixGrantMenu(grants).openMenu(sender));
                } else {
                    sender.sendMessage(ChatColor.RED + response.getErrorMessage());
                }
            });
        });
    }
}

