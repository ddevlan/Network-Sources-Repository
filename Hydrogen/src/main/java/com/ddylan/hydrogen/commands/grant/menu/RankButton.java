/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.ddylan.library.menu.Button
 *  com.ddylan.library.util.TimeUtils
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.conversations.Conversable
 *  org.bukkit.conversations.Conversation
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.ConversationFactory
 *  org.bukkit.conversations.ConversationPrefix
 *  org.bukkit.conversations.NullConversationPrefix
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.ddylan.hydrogen.commands.grant.menu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.library.menu.Button;
import com.ddylan.library.util.ItemBuilder;
import com.ddylan.library.util.TimeUtil;
import com.google.common.collect.Lists;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankButton extends Button {

    private final String targetName;
    private final UUID targetUUID;
    private final Rank rank;

    public String getName(Player player) {
        return this.rank.getFormattedName();
    }

    public List<String> getDescription(Player player) {
        ArrayList description = Lists.newArrayList();
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 30));
        description.add(ChatColor.BLUE + "Click to grant " + ChatColor.WHITE + this.targetName + ChatColor.BLUE + " the " + ChatColor.WHITE + this.rank.getFormattedName() + ChatColor.BLUE + " rank.");
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 30));
        return description;
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return this.getColor().getWoolData();
    }

    public void clicked(final Player player, int i, ClickType clickType) {
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(Hydrogen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt(){

            public String getPromptText(ConversationContext context) {
                return ChatColor.YELLOW + "Please type a reason for this grant to be added, or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
            }

            public Prompt acceptInput(ConversationContext context, final String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(ChatColor.RED + "Granting cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }
                new BukkitRunnable(){

                    public void run() {
                        RankButton.this.promptTime(player, input);
                    }
                }.runTask(Hydrogen.getInstance());
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }

    private void promptTime(final Player player, final String reason) {
        ConversationFactory factory = new ConversationFactory(Hydrogen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt(){

            public String getPromptText(ConversationContext context) {
                return ChatColor.YELLOW + "Please type a duration for this grant, (\"perm\" for permanent) or type " + ChatColor.RED + "cancel" + ChatColor.YELLOW + " to cancel.";
            }

            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(ChatColor.RED + "Granting cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }
                int duration = -1;
                if (!StringUtils.startsWithIgnoreCase(input, "perm")) {
                    duration = Math.toIntExact(TimeUtil.parseTime(input));
                }
                if (duration <= 1 && !StringUtils.startsWithIgnoreCase(input, "perm")) {
                    context.getForWhom().sendRawMessage(ChatColor.RED + "Invalid duration.");
                    return Prompt.END_OF_CONVERSATION;
                }
                final int finalDuration = duration;
                new BukkitRunnable(){

                    public void run() {
                        new ScopesMenu(false, false, RankButton.this.rank, RankButton.this.targetName, RankButton.this.targetUUID, reason, finalDuration).openMenu(player);
                    }
                }.runTask(Hydrogen.getInstance());
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
    }

    private DyeColor getColor() {
        ChatColor color = ChatColor.getByChar(this.rank.getGameColor().charAt(1));
        switch (color) {
            case DARK_BLUE: {
                return DyeColor.BLUE;
            }
            case DARK_GREEN: {
                return DyeColor.GREEN;
            }
            case DARK_AQUA: 
            case AQUA: {
                return DyeColor.CYAN;
            }
            case DARK_RED: 
            case RED: {
                return DyeColor.RED;
            }
            case DARK_PURPLE: {
                return DyeColor.PURPLE;
            }
            case GOLD: {
                return DyeColor.ORANGE;
            }
            case GRAY: 
            case DARK_GRAY: {
                return DyeColor.GRAY;
            }
            case BLUE: {
                return DyeColor.BLUE;
            }
            case GREEN: {
                return DyeColor.LIME;
            }
            case LIGHT_PURPLE: {
                return DyeColor.PINK;
            }
            case YELLOW: {
                return DyeColor.YELLOW;
            }
            case WHITE: {
                return DyeColor.WHITE;
            }
        }
        return DyeColor.BLACK;
    }

    public RankButton(String targetName, UUID targetUUID, Rank rank) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.rank = rank;
    }
}

