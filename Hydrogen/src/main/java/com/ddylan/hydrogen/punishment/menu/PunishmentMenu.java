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
package com.ddylan.hydrogen.punishment.menu;

import com.ddylan.hydrogen.punishment.Punishment;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.pagination.PaginatedMenu;
import com.ddylan.library.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PunishmentMenu
extends PaginatedMenu {
    private final String targetUUID;
    private final String targetName;
    private final Punishment.PunishmentType type;
    private final Map<Punishment, String> punishments;

    public String getPrePaginatedTitle(Player player) {
        return ChatColor.RED + this.type.getName() + "s";
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(getMaterial(player)).name(getName(player)).build();
            }

            public String getName(Player player) {
                return ChatColor.YELLOW + "Back";
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
                new MainPunishmentMenu(PunishmentMenu.this.targetUUID, PunishmentMenu.this.targetName).openMenu(player);
            }
        });
        return buttons;
    }

    public Map<Integer, Button> getAllPagesButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        int index = 0;
        for (Map.Entry<Punishment, String> entry : this.punishments.entrySet()) {
            buttons.put(index, new PunishmentButton(entry.getKey(), entry.getValue(), player.hasPermission("minehq.punishments.view.reason")));
            ++index;
        }
        return buttons;
    }

    public PunishmentMenu(String targetUUID, String targetName, Punishment.PunishmentType type, Map<Punishment, String> punishments) {
        this.targetUUID = targetUUID;
        this.targetName = targetName;
        this.type = type;
        this.punishments = punishments;
    }
}

