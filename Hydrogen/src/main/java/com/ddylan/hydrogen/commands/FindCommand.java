/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.server.Server;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.UUID;

public class FindCommand {
    @Command(names={"find"}, permission="hydrogen.find", description="See the server an user is currently playing on", async=true)
    public static void find(CommandSender sender, @Param(name="player") PunishmentTarget target) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "An error occurred when contacting the Mojang API.");
                return;
            }
            Optional<Server> serverOptional = Hydrogen.getInstance().getServerHandler().find((UUID)uuid);
            if (!serverOptional.isPresent()) {
                sender.sendMessage(ChatColor.RED + target.getName() + " is currently not on the network.");
                return;
            }
            Server server = serverOptional.get();
            sender.sendMessage(ChatColor.BLUE + target.getName() + ChatColor.YELLOW + " is currently on " + ChatColor.BLUE + server.getDisplayName() + ChatColor.YELLOW + ".");
        });
    }
}

