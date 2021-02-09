/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.ddylan.hydrogen.punishment.meta.defaults;

import com.ddylan.hydrogen.Hydrogen;
import com.ddylan.hydrogen.listener.BanMetaListener;
import com.ddylan.hydrogen.punishment.meta.PunishmentMeta;
import com.ddylan.hydrogen.punishment.meta.PunishmentMetaFetcher;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HPunishmentMetaFetcher
extends PunishmentMetaFetcher {
    public HPunishmentMetaFetcher() {
        super(Hydrogen.getInstance());
    }

    @Override
    public PunishmentMeta fetch(UUID target) {
        Player player = Bukkit.getPlayer(target);
        HashMap<String, Object> map = Maps.newHashMap();
        if (BanMetaListener.getMessages().containsKey(target)) {
            map.put("chatMessages", BanMetaListener.getMessages().get(target));
        }
        if (BanMetaListener.getJoinTime().containsKey(target)) {
            map.put("secondsOnline", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - BanMetaListener.getJoinTime().get(target)));
        }
        if (player != null) {
            map.put("server", Bukkit.getServerName());
            map.put("location", player.getLocation());
            map.put("ip", player.getAddress().getAddress().getHostAddress());
        }
        return PunishmentMeta.of(map);
    }
}

