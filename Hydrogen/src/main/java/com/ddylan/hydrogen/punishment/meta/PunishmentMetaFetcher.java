/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.ddylan.hydrogen.punishment.meta;

import com.ddylan.hydrogen.punishment.Punishment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public abstract class PunishmentMetaFetcher {
    private JavaPlugin plugin;

    public abstract PunishmentMeta fetch(UUID var1);

    public PunishmentMeta fetch(UUID target, Punishment.PunishmentType type) {
        return this.fetch(target);
    }

    public PunishmentMetaFetcher(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}

