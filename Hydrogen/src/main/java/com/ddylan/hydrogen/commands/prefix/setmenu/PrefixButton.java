/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.ddylan.library.menu.Button
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package com.ddylan.hydrogen.commands.prefix.setmenu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.prefix.Prefix;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.library.menu.Button;
import com.ddylan.library.util.ItemBuilder;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PrefixButton extends Button {

    private final String targetName;
    private final UUID targetUUID;
    private final Prefix prefix;
    private final boolean active;
    private final boolean authorized;

    public String getName(Player player) {
        return ChatColor.translateAlternateColorCodes('&', this.prefix.getButtonName());
    }

    public List<String> getDescription(Player player) {
        ArrayList description = Lists.newArrayList();
        if (this.active) {
            description.add(ChatColor.RED + "This is already your prefix.");
            description.add(ChatColor.RED + "Click to disable.");
        } else if (this.authorized) {
            description.add(ChatColor.GRAY + "Click to make this your prefix.");
        } else if (this.prefix.isPurchaseable()) {
            if (this.prefix.getButtonDescription().contains("%newline%")) {
                Arrays.stream(this.prefix.getButtonDescription().split("%newline%")).map(string -> ChatColor.translateAlternateColorCodes('&', string)).forEach(description::add);
            } else {
                description.add(ChatColor.translateAlternateColorCodes('&', this.prefix.getButtonDescription()));
            }
        } else {
            description.add(ChatColor.GRAY + "This prefix is unavailable and unpurchaseable.");
        }
        return description;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return this.getColor().getWoolData();
    }

    public void clicked(Player player, int i, ClickType clickType) {
        if (this.active) {
            Profile playerProfile = Hydrogen.getInstance().getProfileHandler().getProfile(player.getUniqueId()).get();
            playerProfile.setActivePrefix(null);
            playerProfile.updatePlayer(player);
            player.sendMessage(ChatColor.GREEN + "Removed prefix.");
            return;
        }
        if (!this.authorized) {
            player.sendMessage(ChatColor.RED + "You can't use this prefix!");
            if (this.prefix.isPurchaseable()) {
                player.sendMessage(ChatColor.RED + "But it's available for a limited time at store.veltpvp.com!");
            }
            return;
        }
        Profile playerProfile = Hydrogen.getInstance().getProfileHandler().getProfile(player.getUniqueId()).get();
        playerProfile.setActivePrefix(this.prefix);
        playerProfile.updatePlayer(player);
        player.sendMessage(ChatColor.GREEN + "Updated prefix.");
    }

    private DyeColor getColor() {
        return this.active ? DyeColor.GREEN : (this.authorized ? DyeColor.RED : DyeColor.GRAY);
    }

    public PrefixButton(String targetName, UUID targetUUID, Prefix prefix, boolean active, boolean authorized) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.prefix = prefix;
        this.active = active;
        this.authorized = authorized;
    }
}

