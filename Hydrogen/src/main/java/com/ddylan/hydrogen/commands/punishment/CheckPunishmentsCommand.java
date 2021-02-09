/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.commands.punishment;

import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.punishment.menu.MainPunishmentMenu;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CheckPunishmentsCommand {
    @Command(names={"checkpunishments", "cp", "c"}, permission="minehq.punishment.view", description="Check a user's punishments")
    public static void checkPunishments(Player sender, @Param(name="target") PunishmentTarget target) {
        target.resolveUUID(uuid -> {
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "An error occurred when contacting the Mojang API.");
                return;
            }
            new MainPunishmentMenu(uuid.toString(), target.getName()).openMenu(sender);
        });
    }
}

