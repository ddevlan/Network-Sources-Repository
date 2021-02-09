/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  com.ddylan.library.command.Param
 *  com.ddylan.library.util.Callback
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.ddylan.hydrogen.commands.prefix;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.commands.prefix.setmenu.PrefixMenu;
import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.library.command.Command;
import com.ddylan.library.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PrefixGrantCommand {
    @Command(names={"grantprefix", "prefixgrant"}, permission="hydrogen.grantprefix", description="Add a prefix grant to a users account - Cute GUI!")
    public static void grant(Player sender, final @Param(name="player") PunishmentTarget target) {
        target.resolveUUID(uuid -> new BukkitRunnable() {
            @Override
            public void run() {
                new PrefixMenu(target.getName(), uuid).openMenu(sender);
            }
        }.runTask(Hydrogen.getInstance()));
    }
}

