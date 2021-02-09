/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package com.ddylan.hydrogen.commands.management;

import com.ddylan.hydrogen.Settings;
import com.ddylan.library.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Enable2FACommand {
    @Command(names={"enable2fa"}, permission="super", description="Enable two-factor authentication on the server")
    public static void enable2fa(CommandSender sender) {
        Settings.setForceStaffTotp(true);
        Settings.save();
        sender.sendMessage(ChatColor.GREEN + "2FA set to \"" + ChatColor.WHITE + true + ChatColor.GREEN + "\"");
    }
}

