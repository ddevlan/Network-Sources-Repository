/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Ints
 *  com.ddylan.library.qLib
 *  org.bukkit.ChatColor
 */
package com.ddylan.hydrogen.rank;

import com.ddylan.hydrogen.Settings;
import com.ddylan.library.LibraryPlugin;
import com.google.common.primitives.Ints;
import org.bukkit.ChatColor;

import java.util.Comparator;

public final class Rank {

    private final String id;
    private final String inheritsFromId;
    private final int generalWeight;
    private final int displayWeight;
    private final String displayName;
    private final String gamePrefix;
    private final String gameColor;
    private final boolean staffRank;
    private final boolean grantRequiresTotp;
    private final String queueMessage;
    public static final Comparator<Rank> GENERAL_WEIGHT_COMPARATOR = (a, b) -> Ints.compare(b.getGeneralWeight(), a.getGeneralWeight());
    public static final Comparator<Rank> DISPLAY_WEIGHT_COMPARATOR = (a, b) -> Ints.compare(b.getDisplayWeight(), a.getDisplayWeight());

    public String getFormattedName() {
        return this.getGameColor() + this.getDisplayName();
    }

    public String toString() {
        return LibraryPlugin.getInstance().getXJedis().GSON.toJson(this);
    }

    public String getGameColor() {
        if (Settings.isClean()) {
            return ChatColor.translateAlternateColorCodes('&', this.gameColor.replace("&l", ""));
        }
        return ChatColor.translateAlternateColorCodes('&', this.gameColor);
    }

    public String getGamePrefix() {
        if (Settings.isClean()) {
            return ChatColor.translateAlternateColorCodes('&', this.gamePrefix.replace("&l", ""));
        }
        return ChatColor.translateAlternateColorCodes('&', this.gamePrefix);
    }

    public boolean equals(Object obj) {
        return obj instanceof Rank && ((Rank)obj).getId().equals(this.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public Rank(String id, String inheritsFromId, int generalWeight, int displayWeight, String displayName, String gamePrefix, String gameColor, boolean staffRank, boolean grantRequiresTotp, String queueMessage) {
        this.id = id;
        this.inheritsFromId = inheritsFromId;
        this.generalWeight = generalWeight;
        this.displayWeight = displayWeight;
        this.displayName = displayName;
        this.gamePrefix = gamePrefix;
        this.gameColor = gameColor;
        this.staffRank = staffRank;
        this.grantRequiresTotp = grantRequiresTotp;
        this.queueMessage = queueMessage;
    }

    public String getId() {
        return this.id;
    }

    public String getInheritsFromId() {
        return this.inheritsFromId;
    }

    public int getGeneralWeight() {
        return this.generalWeight;
    }

    public int getDisplayWeight() {
        return this.displayWeight;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean isStaffRank() {
        return this.staffRank;
    }

    public boolean isGrantRequiresTotp() {
        return this.grantRequiresTotp;
    }

    public String getQueueMessage() {
        return this.queueMessage;
    }
}

