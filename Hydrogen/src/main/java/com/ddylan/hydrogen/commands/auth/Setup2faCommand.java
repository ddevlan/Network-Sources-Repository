/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.Command
 *  org.bukkit.ChatColor
 *  org.bukkit.conversations.Conversable
 *  org.bukkit.conversations.Conversation
 *  org.bukkit.conversations.ConversationFactory
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.commands.auth;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.totp.prompt.InitialDisclaimerPrompt;
import com.ddylan.library.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public final class Setup2faCommand {
    @Command(names={"setup2fa", "2fasetup"}, permission="minehq.totp.setup", description="Sign up to use 2FA to verify your identity")
    public static void setup2fa(Player sender) {
        if (Hydrogen.getInstance().getProfileHandler().getTotpEnabled().contains(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You already have 2FA setup!");
            return;
        }
        ConversationFactory factory = new ConversationFactory(Hydrogen.getInstance()).withFirstPrompt(new InitialDisclaimerPrompt()).withLocalEcho(false).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }
}

