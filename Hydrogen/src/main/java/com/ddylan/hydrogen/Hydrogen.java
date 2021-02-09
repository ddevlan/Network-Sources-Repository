/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.ddylan.library.command.FrozenCommandHandler
 *  com.ddylan.library.command.ParameterType
 *  com.ddylan.library.nametag.FrozenNametagHandler
 *  com.ddylan.library.nametag.NametagProvider
 *  com.ddylan.library.util.ClassUtils
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.ddylan.hydrogen;

import com.ddylan.hydrogen.commands.punishment.parameter.PunishmentTarget;
import com.ddylan.hydrogen.nametag.HNametagProvider;
import com.ddylan.hydrogen.permission.PermissionHandler;
import com.ddylan.hydrogen.prefix.PrefixHandler;
import com.ddylan.hydrogen.profile.ProfileHandler;
import com.ddylan.hydrogen.punishment.PunishmentHandler;
import com.ddylan.hydrogen.rank.RankHandler;
import com.ddylan.hydrogen.server.ServerHandler;
import com.ddylan.library.LibraryPlugin;
import com.ddylan.library.util.ClassUtils;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class Hydrogen
extends JavaPlugin {

    private static Hydrogen instance;
    private static final OkHttpClient okHttpClient;
    private RankHandler rankHandler;
    private ServerHandler serverHandler;
    private ProfileHandler profileHandler;
    private PermissionHandler permissionHandler;
    private PunishmentHandler punishmentHandler;
    private PrefixHandler prefixHandler;

    public void onEnable() {
        instance = this;
        Settings.load();
        this.setupHandlers();
        this.setupCommands();
        this.setupListeners();
        LibraryPlugin.getInstance().getNametagHandler().registerProvider(new HNametagProvider());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "MHQ-Queue");
    }

    public void onDisable() {
        instance = null;
    }

    private void setupHandlers() {
        this.rankHandler = new RankHandler();
        this.serverHandler = new ServerHandler();
        this.profileHandler = new ProfileHandler();
        this.permissionHandler = new PermissionHandler();
        this.punishmentHandler = new PunishmentHandler();
        this.prefixHandler = new PrefixHandler();
    }

    private void setupCommands() {
        LibraryPlugin.getInstance().getCommandHandler().registerPackage(this, "net.frozenorb.hydrogen.commands");
        LibraryPlugin.getInstance().getCommandHandler().registerParameterType(PunishmentTarget.class, new PunishmentTarget.Type());
    }

    private void setupListeners() {
        ClassUtils.getClassesInPackage((Plugin)this, (String)"net.frozenorb.hydrogen.listener").stream().filter(Listener.class::isAssignableFrom).forEach(clazz -> {
            try {
                Bukkit.getPluginManager().registerEvents((Listener)clazz.newInstance(), this);
            }
            catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    public static Hydrogen getInstance() {
        return instance;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public RankHandler getRankHandler() {
        return this.rankHandler;
    }

    public ServerHandler getServerHandler() {
        return this.serverHandler;
    }

    public ProfileHandler getProfileHandler() {
        return this.profileHandler;
    }

    public PermissionHandler getPermissionHandler() {
        return this.permissionHandler;
    }

    public PunishmentHandler getPunishmentHandler() {
        return this.punishmentHandler;
    }

    public PrefixHandler getPrefixHandler() {
        return this.prefixHandler;
    }

    static {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(2L, TimeUnit.SECONDS).writeTimeout(2L, TimeUnit.SECONDS).readTimeout(2L, TimeUnit.SECONDS).build();
    }
}

