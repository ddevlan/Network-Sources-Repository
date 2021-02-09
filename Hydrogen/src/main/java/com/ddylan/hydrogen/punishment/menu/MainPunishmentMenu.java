/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 *  com.google.common.primitives.Longs
 *  com.ddylan.library.menu.Button
 *  com.ddylan.library.menu.Menu
 *  com.ddylan.library.qLib
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.punishment.menu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.hydrogen.punishment.Punishment;
import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import com.ddylan.library.util.ItemBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Longs;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainPunishmentMenu extends Menu {

    private final String targetUUID;
    private final String targetName;

    public String getTitle(Player player) {
        return ChatColor.BLUE + "Punishments - " + this.targetName;
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        if (player.hasPermission("minehq.punishments.view.blacklist")) {
            buttons.put(1, this.button(Punishment.PunishmentType.WARN));
            buttons.put(3, this.button(Punishment.PunishmentType.MUTE));
            buttons.put(5, this.button(Punishment.PunishmentType.BAN));
            buttons.put(7, this.button(Punishment.PunishmentType.BLACKLIST));
        } else {
            buttons.put(1, this.button(Punishment.PunishmentType.WARN));
            buttons.put(4, this.button(Punishment.PunishmentType.MUTE));
            buttons.put(7, this.button(Punishment.PunishmentType.BAN));
        }
        return buttons;
    }

    private Button button(final Punishment.PunishmentType type) {
        return new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
            }

            public String getName(Player player) {
                return ChatColor.RED + type.getName() + "s";
            }

            public List<String> getDescription(Player player) {
                return null;
            }

            public Material getMaterial(Player player) {
                return Material.WOOL;
            }

            public byte getDamageValue(Player player) {
                if (type == Punishment.PunishmentType.WARN) {
                    return DyeColor.YELLOW.getWoolData();
                }
                if (type == Punishment.PunishmentType.MUTE) {
                    return DyeColor.ORANGE.getWoolData();
                }
                if (type == Punishment.PunishmentType.BAN) {
                    return DyeColor.RED.getWoolData();
                }
                return DyeColor.BLACK.getWoolData();
            }

            public void clicked(Player player, int i, ClickType clickType) {
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "Loading " + MainPunishmentMenu.this.targetName + "'s " + type.getName() + "s...");
                Bukkit.getScheduler().scheduleAsyncDelayedTask(Hydrogen.getInstance(), () -> {
                    //TODO: endpoint
                    RequestResponse response = RequestHandler.get("/punishments", ImmutableMap.of("user", targetUUID));
                    if (response.wasSuccessful()) {
                        List<Punishment> allPunishments = LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<List<Punishment>>(){}.getType());
                        allPunishments.sort((first, second) -> Longs.compare(second.getAddedAt(), first.getAddedAt()));
                        LinkedHashMap<Punishment, String> punishments = new LinkedHashMap();
                        allPunishments.stream().filter(punishment -> punishment.getType() == type).forEach(punishment -> punishments.put(punishment, punishment.resolveAddedBy()));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Hydrogen.getInstance(), () -> new PunishmentMenu(MainPunishmentMenu.this.targetUUID, MainPunishmentMenu.this.targetName, type, punishments).openMenu(player));
                    } else {
                        player.sendMessage(ChatColor.RED + response.getErrorMessage());
                    }
                });
            }
        };
    }

    public MainPunishmentMenu(String targetUUID, String targetName) {
        this.targetUUID = targetUUID;
        this.targetName = targetName;
    }
}

