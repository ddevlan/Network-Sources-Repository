/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.ddylan.library.menu.Button
 *  net.minecraft.util.org.apache.commons.lang3.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package com.ddylan.hydrogen.commands.prefix.menu;

import com.ddylan.hydrogen.server.Server;
import com.ddylan.library.menu.Button;
import com.ddylan.library.util.ItemBuilder;
import com.google.common.collect.Lists;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScopeButton extends Button {

    private final ScopesMenu parent;
    private final Server scope;

    public String getName(Player player) {
        boolean status = this.parent.getStatus().get(this.scope.getId());
        return (status ? ChatColor.GREEN : ChatColor.RED) + this.scope.getId();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
    }

    public List<String> getDescription(Player player) {
        boolean status = this.parent.getStatus().get(this.scope.getId());
        ArrayList description = Lists.newArrayList();
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 30));
        if (status) {
            description.add(ChatColor.BLUE + "Click to " + ChatColor.RED + "remove " + ChatColor.YELLOW + this.scope.getId() + ChatColor.BLUE + " from this grant's scopes.");
        } else {
            description.add(ChatColor.BLUE + "Click to " + ChatColor.GREEN + "add " + ChatColor.YELLOW + this.scope.getId() + ChatColor.BLUE + " to this grant's scopes.");
        }
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 30));
        return description;
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        boolean status = this.parent.getStatus().get(this.scope.getId());
        return status ? DyeColor.LIME.getWoolData() : DyeColor.GRAY.getWoolData();
    }

    public void clicked(Player player, int i, ClickType clickType) {
        this.parent.getStatus().put(this.scope.getId(), this.parent.getStatus().getOrDefault(this.scope.getId(), false) == false);
        this.parent.setGlobal(false);
    }

    public ScopeButton(ScopesMenu parent, Server scope) {
        this.parent = parent;
        this.scope = scope;
    }
}

