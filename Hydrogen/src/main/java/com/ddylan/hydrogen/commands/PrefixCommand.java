/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.commands.prefix.setmenu.PrefixMenu;
import com.ddylan.library.command.Command;
import org.bukkit.entity.Player;

public class PrefixCommand {
    @Command(names={"prefix", "setprefix", "setprefixes"}, permission="")
    public static void prefix(Player sender) {
        new PrefixMenu(sender.getName(), sender.getUniqueId()).openMenu(sender);
    }
}

