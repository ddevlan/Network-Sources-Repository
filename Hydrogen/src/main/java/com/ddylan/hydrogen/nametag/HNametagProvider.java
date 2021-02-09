/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.nametag.NametagInfo
 *  com.ddylan.library.nametag.NametagProvider
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.nametag;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.profile.Profile;
import com.ddylan.hydrogen.rank.Rank;
import com.ddylan.library.nametag.NametagInfo;
import com.ddylan.library.nametag.NametagProvider;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HNametagProvider
extends NametagProvider {
    public HNametagProvider() {
        super("Hydrogen Provider", 1);
    }

    public NametagInfo fetchNametag(Player player, Player watcher) {
        Optional<Profile> profileOptional = Hydrogen.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        if (!profileOptional.isPresent()) {
            return HNametagProvider.createNametag("", "");
        }
        Profile profile = profileOptional.get();
        Rank bestDisplayRank = profile.getBestDisplayRank();
        if (bestDisplayRank == null) {
            return HNametagProvider.createNametag("", "");
        }
        return HNametagProvider.createNametag(bestDisplayRank.getGameColor(), "");
    }
}

