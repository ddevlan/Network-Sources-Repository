/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.ddylan.library.menu.Button
 *  com.ddylan.library.menu.pagination.PaginatedMenu
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package com.ddylan.hydrogen.prefix.menu;

import com.ddylan.hydrogen.prefix.PrefixGrant;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.pagination.PaginatedMenu;
import com.ddylan.library.util.ItemBuilder;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixGrantMenu
extends PaginatedMenu {
    private final Map<PrefixGrant, String> prefixGrants;

    public String getPrePaginatedTitle(Player player) {
        return (Object)ChatColor.RED + "Prefix Grants";
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
            }

            public String getName(Player player) {
                return (Object)ChatColor.YELLOW + "Back";
            }

            public List<String> getDescription(Player player) {
                return null;
            }

            public Material getMaterial(Player player) {
                return Material.PAPER;
            }

            public byte getDamageValue(Player player) {
                return 0;
            }

            public void clicked(Player player, int i, ClickType clickType) {
                player.closeInventory();
            }
        });
        return buttons;
    }

    public Map<Integer, Button> getAllPagesButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        int index = 0;
        for (Map.Entry<PrefixGrant, String> entry : this.prefixGrants.entrySet()) {
            buttons.put(index, new PrefixGrantButton(entry.getKey(), entry.getValue()));
            ++index;
        }
        return buttons;
    }

    public PrefixGrantMenu(Map<PrefixGrant, String> prefixGrants) {
        this.prefixGrants = prefixGrants;
    }
}

