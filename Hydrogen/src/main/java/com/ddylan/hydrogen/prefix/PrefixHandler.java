/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.ddylan.library.qLib
 *  net.minecraft.util.com.google.common.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.ddylan.hydrogen.prefix;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.connection.RequestHandler;
import com.ddylan.hydrogen.connection.RequestResponse;
import com.ddylan.library.LibraryPlugin;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.util.*;

public class PrefixHandler {
    private List<Prefix> prefixes = Lists.newArrayList();
    private Map<String, Prefix> prefixCache;

    public PrefixHandler() {
        this.refresh();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hydrogen.getInstance(), this::refresh, 0L, 1200L);
    }

    public void refresh() {
        //TODO: endpoint
        RequestResponse response = RequestHandler.get("/prefixes");
        if (response.wasSuccessful()) {
            this.prefixes = LibraryPlugin.getInstance().getXJedis().PLAIN_GSON.fromJson(response.getResponse(), new TypeToken<List<Prefix>>(){}.getType());
            HashMap<String, Prefix> newPrefixCache = Maps.newHashMap();
            for (Prefix prefix : this.prefixes) {
                newPrefixCache.put(prefix.getId(), prefix);
            }
            this.prefixCache = newPrefixCache;
        } else {
            Bukkit.getLogger().warning("PrefixHandler - Could not retrieve prefixes from API: " + response.getErrorMessage());
        }
    }

    public Optional<Prefix> getPrefix(String parse) {
        return Optional.ofNullable(this.prefixCache.get(parse));
    }

    public List<Prefix> getPrefixes() {
        return new ArrayList<Prefix>(this.prefixes);
    }
}

