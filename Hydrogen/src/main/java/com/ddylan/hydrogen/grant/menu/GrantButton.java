/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Lists
 *  com.ddylan.library.menu.Button
 *  com.ddylan.library.util.TimeUtils
 *  com.ddylan.library.util.UUIDUtils
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
package com.ddylan.hydrogen.grant.menu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.grant.Grant;
import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.menu.Button;
import com.ddylan.library.util.ItemBuilder;
import com.ddylan.library.util.TimeUtil;
import com.google.common.collect.ImmutableMap;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GrantButton extends Button {

    private final Grant grant;
    private final String addedByResolved;

    public String getName(Player player) {
        return ChatColor.YELLOW + TimeUtil.formatIntoCalendarString(new Date(this.grant.getAddedAt()));
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
    }

    public List<String> getDescription(Player player) {
        ArrayList description = Lists.newArrayList();
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 25));
        int seconds = this.grant.getExpiresAt() > 0L ? TimeUtil.getSecondsBetween(new Date(), new Date(this.grant.getExpiresAt())) : 0;
        String by = this.grant.getAddedBy() == null ? "Console" : this.addedByResolved;
        description.add(ChatColor.YELLOW + "By: " + ChatColor.RED + by);
        description.add(ChatColor.YELLOW + "Reason: " + ChatColor.RED + this.grant.getReason());
        description.add(ChatColor.YELLOW + "Scopes: " + ChatColor.RED + (this.grant.getScopes().isEmpty() ? "Global" : this.grant.getScopes()));
        description.add(ChatColor.YELLOW + "Rank: " + ChatColor.RED + this.grant.getRank());
        if (this.grant.isActive()) {
            if (this.grant.getExpiresAt() != 0L) {
                description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 25));
                description.add(ChatColor.YELLOW + "Time remaining: " + ChatColor.RED + TimeUtil.formatIntoDetailedString(seconds));
            } else {
                description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 25));
                description.add(ChatColor.YELLOW + "This is a permanent grant.");
            }
            if (player.hasPermission("minehq.grant.remove." + this.grant.getRank())) {
                description.add("");
                description.add(ChatColor.RED.toString() + ChatColor.BOLD + "Click to remove");
                description.add(ChatColor.RED.toString() + ChatColor.BOLD + "this grant");
            }
        } else if (this.grant.isRemoved()) {
            String removedBy = this.grant.getRemovedBy() == null ? "Console" : LibraryPlugin.getInstance().getUuidCache().name((UUID)this.grant.getRemovedBy());
            description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 25));
            description.add(ChatColor.RED + "Removed:");
            description.add(ChatColor.YELLOW + removedBy + ": " + ChatColor.RED + this.grant.getRemovalReason());
            description.add(ChatColor.RED + "at " + ChatColor.YELLOW + TimeUtil.formatIntoCalendarString(new Date(this.grant.getRemovedAt())));
            if (this.grant.getExpiresAt() != 0L) {
                description.add("");
                description.add(ChatColor.YELLOW + "Duration: " + TimeUtil.formatIntoDetailedString((int)((this.grant.getExpiresAt() - this.grant.getAddedAt()) / 1000L) + 1));
            }
        } else if (this.grant.isExpired()) {
            description.add(ChatColor.YELLOW + "Duration: " + TimeUtil.formatIntoDetailedString((int)((this.grant.getExpiresAt() - this.grant.getAddedAt()) / 1000L) + 1));
            description.add(ChatColor.GREEN + "Expired");
        }
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 25));
        return description;
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return this.grant.isActive() ? DyeColor.LIME.getWoolData() : DyeColor.RED.getWoolData();
    }

    public void clicked(final Player player, int i, ClickType clickType) {
        if (!player.hasPermission("minehq.grant.remove." + this.grant.getRank()) || !this.grant.isActive()) {
            return;
        }
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(Hydrogen.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt(){

            public String getPromptText(ConversationContext context) {
                return "\u00a7aType a reason to be used when removing this grant. Type \u00a7cno\u00a7a to quit.";
            }

            public Prompt acceptInput(ConversationContext cc, final String s) {
                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Grant removal aborted.");
                } else {
                    new BukkitRunnable(){

                        public void run() {
                            RequestResponse response = RequestHandler.delete("/grants/" + GrantButton.this.grant.getId(), ImmutableMap.of("removedBy", player.getUniqueId(), "removedByIp", player.getAddress().getAddress().getHostAddress(), "reason", s));
                            if (response.wasSuccessful()) {
                                player.sendMessage(ChatColor.GREEN + "Removed grant successfully.");
                            } else {
                                player.sendMessage(ChatColor.RED + response.getErrorMessage());
                            }
                        }
                    }.runTaskAsynchronously(Hydrogen.getInstance());
                }
                return Prompt.END_OF_CONVERSATION;
            }
        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(60).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }

    public GrantButton(Grant grant, String addedByResolved) {
        this.grant = grant;
        this.addedByResolved = addedByResolved;
    }
}

