/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  net.frozenorb.qlib.command.Command
 *  net.frozenorb.qlib.menu.Button
 *  net.frozenorb.qlib.menu.Menu
 *  net.frozenorb.qlib.menu.menus.ConfirmMenu
 *  net.frozenorb.qlib.util.Callback
 *  org.apache.commons.lang.WordUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.commands;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.Settings;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.library.command.Command;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import com.ddylan.library.menu.menus.ConfirmMenu;
import com.ddylan.library.util.ItemBuilder;
import com.ddylan.library.util.callback.TypeCallback;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColorsCommand {
    private static final Button BLACK_GLASS_PANE = Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, " ");
    private static final ColorsCommand INSTANCE = new ColorsCommand();
    private static final byte WHITE = 0;
    private static final byte ORANGE = 1;
    private static final byte LIGHT_BLUE = (byte)3;
    private static final byte YELLOW = (byte)4;
    private static final byte LIME = (byte)5;
    private static final byte PINK = (byte)6;
    private static final byte GRAY = (byte)7;
    private static final byte LIGHT_GRAY = (byte)8;
    private static final byte CYAN = (byte)9;
    private static final byte PURPLE = (byte)10;
    private static final byte BLUE = (byte)11;
    private static final byte GREEN = (byte)13;
    private static final byte RED = (byte)14;

    @Command(names={"setcolors", "colors", "colours", "setcolours", "color", "colour", "setcolor", "setcolour"}, permission="hydrogen.colors")
    public static void colors(Player sender) {
        INSTANCE.new IconColorMenu().openMenu(sender);
    }

    private Map<Integer, Button> fillMapWithPanes(Map<Integer, Button> map) {
        for (int i = 0; i < 36; ++i) {
            map.putIfAbsent(i, BLACK_GLASS_PANE);
        }
        return map;
    }

    private Button getIconButton(Player player, final ChatColor chatColor, final byte woolColor) {
        return new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
            }

            public String getName(Player player) {
                return chatColor.toString() + WordUtils.capitalizeFully(chatColor.name().toLowerCase().replace('_', ' '));
            }

            public List<String> getDescription(Player player) {
                return Lists.newArrayList();
            }

            public Material getMaterial(Player player) {
                return Material.WOOL;
            }

            public void clicked(Player player, int slot, ClickType clickType) {
                new NameColorMenu(chatColor).openMenu(player);
            }

            public byte getDamageValue(Player player) {
                return woolColor;
            }
        };
    }

    private Button getNameButton(Player player, final ChatColor iconColor, final ChatColor nameColor, final byte woolColor) {
        return new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(getMaterial(player)).lore(getDescription(player)).name(getName(player)).build();
            }

            public String getName(Player player) {
                return nameColor.toString() + WordUtils.capitalizeFully(nameColor.name().toLowerCase().replace('_', ' '));
            }

            public List<String> getDescription(Player player) {
                return Lists.newArrayList();
            }

            public Material getMaterial(Player player) {
                return Material.WOOL;
            }

            public void clicked(final Player player, int slot, ClickType clickType) {
                new ConfirmMenu("Confirm", (TypeCallback<Boolean>) data -> {
                    if (data) {
                        ColorsCommand.this.setColors(player, iconColor, nameColor);
                        player.sendMessage(ChatColor.GREEN + "Updated icon & name colors.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Icon & name color change aborted.");
                    }
                }, false, Button.placeholder(Material.STAINED_GLASS, (byte) 10, "")).openMenu(player);
            }

            public byte getDamageValue(Player player) {
                return woolColor;
            }
        };
    }

    private void setColors(Player player, ChatColor iconColor, ChatColor nameColor) {
        Optional<Profile> profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        if (!profileOptional.isPresent()) {
            return;
        }
        Profile profile = profileOptional.get();
        profile.setIconColor(iconColor);
        profile.setNameColor(nameColor);
        Rank bestDisplayRank = profile.getBestDisplayRank();
        String gameColor = bestDisplayRank.getGameColor();
        if (bestDisplayRank.getId().equals("velt-plus")) {
            gameColor = Objects.firstNonNull(nameColor, (Object)ChatColor.WHITE).toString() + ChatColor.BOLD;
        }
        player.setDisplayName(gameColor + player.getName() + ChatColor.RESET);
        String gamePrefix = bestDisplayRank.getGamePrefix();
        if (bestDisplayRank.getId().equals("velt-plus")) {
            gamePrefix = Objects.firstNonNull(iconColor, (Object)ChatColor.GRAY).toString() + "\u2738" + gameColor + ChatColor.BOLD;
        }
        if (profile.getActivePrefix() != null && !Settings.isClean()) {
            gamePrefix = gamePrefix + ChatColor.translateAlternateColorCodes('&', profile.getActivePrefix().getPrefix());
        }
        player.setMetadata("HydrogenPrefix", new FixedMetadataValue(Hydrogen.getInstance(), gamePrefix));
        Bukkit.getScheduler().runTaskAsynchronously(Hydrogen.getInstance(), () -> RequestHandler.post("/users/" + player.getUniqueId().toString() + "/colors", ImmutableMap.of("iconColor", iconColor.name(), "nameColor", nameColor.name())));
    }

    private class NameColorMenu
    extends Menu {
        private final ChatColor iconColor;

        private NameColorMenu(ChatColor iconColor) {
            this.iconColor = iconColor;
        }

        public String getTitle(Player player) {
            return "Choose your name color";
        }

        public Map<Integer, Button> getButtons(Player player) {
            HashMap<Integer, Button> buttons = new HashMap<>();
            buttons.put(10, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.WHITE, WHITE));
            buttons.put(11, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.GOLD, ORANGE));
            buttons.put(12, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.LIGHT_PURPLE, PINK));
            buttons.put(13, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.AQUA, LIGHT_BLUE));
            buttons.put(14, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.YELLOW, YELLOW));
            buttons.put(15, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.GREEN, LIME));
            buttons.put(16, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.DARK_PURPLE, PURPLE));
            buttons.put(19, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.DARK_GRAY, GRAY));
            buttons.put(20, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.GRAY, LIGHT_GRAY));
            buttons.put(21, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.DARK_AQUA, CYAN));
            buttons.put(22, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.DARK_BLUE, BLUE));
            buttons.put(23, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.RED, RED));
            buttons.put(24, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.DARK_RED, RED));
            buttons.put(25, ColorsCommand.this.getNameButton(player, this.iconColor, ChatColor.DARK_GREEN, GREEN));
            return ColorsCommand.this.fillMapWithPanes(buttons);
        }

        public int size(Map<Integer, Button> buttons) {
            return 36;
        }
    }

    public class IconColorMenu extends Menu {
        public String getTitle(Player player) {
            return "Choose your icon color";
        }

        public Map<Integer, Button> getButtons(Player player) {
            HashMap<Integer, Button> buttons = new HashMap<>();
            buttons.put(10, ColorsCommand.this.getIconButton(player, ChatColor.WHITE, WHITE));
            buttons.put(11, ColorsCommand.this.getIconButton(player, ChatColor.GOLD, ORANGE));
            buttons.put(12, ColorsCommand.this.getIconButton(player, ChatColor.LIGHT_PURPLE, PINK));
            buttons.put(13, ColorsCommand.this.getIconButton(player, ChatColor.AQUA, LIGHT_BLUE));
            buttons.put(14, ColorsCommand.this.getIconButton(player, ChatColor.YELLOW, YELLOW));
            buttons.put(15, ColorsCommand.this.getIconButton(player, ChatColor.GREEN, LIME));
            buttons.put(16, ColorsCommand.this.getIconButton(player, ChatColor.DARK_PURPLE, PURPLE));
            buttons.put(19, ColorsCommand.this.getIconButton(player, ChatColor.DARK_GRAY, GRAY));
            buttons.put(20, ColorsCommand.this.getIconButton(player, ChatColor.GRAY, LIGHT_GRAY));
            buttons.put(21, ColorsCommand.this.getIconButton(player, ChatColor.DARK_AQUA, CYAN));
            buttons.put(22, ColorsCommand.this.getIconButton(player, ChatColor.DARK_BLUE, BLUE));
            buttons.put(23, ColorsCommand.this.getIconButton(player, ChatColor.RED, RED));
            buttons.put(24, ColorsCommand.this.getIconButton(player, ChatColor.DARK_RED, RED));
            buttons.put(25, ColorsCommand.this.getIconButton(player, ChatColor.DARK_GREEN, GREEN));
            return ColorsCommand.this.fillMapWithPanes(buttons);
        }

        public int size(Map<Integer, Button> buttons) {
            return 36;
        }
    }
}

