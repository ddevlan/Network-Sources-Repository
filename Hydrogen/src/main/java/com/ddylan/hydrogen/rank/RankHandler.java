/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.ddylan.library.qLib
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.rank;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.LibraryPlugin;
import com.google.common.collect.Lists;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RankHandler {
    private List<Rank> ranks = Lists.newArrayList();

    public RankHandler() {
        this.refresh();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hydrogen.getInstance(), this::refresh, 0L, 6000L);
    }

    public void refresh() {
        //TODO: endpoint
        RequestResponse response = RequestHandler.get("/ranks");
        if (response.wasSuccessful()) {
            this.ranks = (List<Rank>)LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<List<Rank>>(){}.getType());
            this.ranks.sort(Rank.DISPLAY_WEIGHT_COMPARATOR);
        } else {
            Bukkit.getLogger().warning("RankHandler - Could not retrieve ranks from API: " + response.getErrorMessage());
        }
    }

    public Optional<Rank> getRank(String parse) {
        for (Rank rank : this.ranks) {
            if (rank.getId().equalsIgnoreCase(parse)) {
                return Optional.of(rank);
            }
            if (!rank.getDisplayName().equalsIgnoreCase(parse)) continue;
            return Optional.of(rank);
        }
        return Optional.empty();
    }

    public List<Rank> getRanks() {
        return new ArrayList<>(this.ranks);
    }
}

