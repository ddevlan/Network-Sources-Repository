/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.ddylan.library.menu.Button
 *  com.ddylan.library.menu.Menu
 *  net.minecraft.util.com.google.common.base.Objects
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.commands.prefix.setmenu;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.prefix.Prefix;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.library.menu.Button;
import com.ddylan.library.menu.Menu;
import net.minecraft.util.com.google.common.base.Objects;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PrefixMenu extends Menu {

    private final String targetName;
    private final UUID targetUUID;

    public String getTitle(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Choose a Prefix";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        List<Prefix> prefixes = Hydrogen.getInstance().getPrefixHandler().getPrefixes();
        Profile playerProfile = Hydrogen.getInstance().getProfileHandler().getProfile(player.getUniqueId()).get();
        Set<Prefix> authorizedPrefixes = playerProfile.getAuthorizedPrefixes();
        for (int i = 0; i < prefixes.size(); ++i) {
            buttons.put(i, new PrefixButton(this.targetName, this.targetUUID, prefixes.get(i), Objects.equal(prefixes.get(i), playerProfile.getActivePrefix()), authorizedPrefixes.contains(prefixes.get(i))));
        }
        return buttons;
    }

    public PrefixMenu(String targetName, UUID targetUUID) {
        this.targetName = targetName;
        this.targetUUID = targetUUID;
    }
}

