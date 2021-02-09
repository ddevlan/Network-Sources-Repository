package com.ddylan.hydrogen.api.util;

import com.ddylan.hydrogen.api.HydrogenAPI;
import com.ddylan.hydrogen.api.model.Player;
import com.ddylan.hydrogen.api.model.Punishment;
import com.ddylan.hydrogen.api.repository.PlayerRepository;
import com.ddylan.hydrogen.api.repository.PrefixGrantRepository;
import com.ddylan.hydrogen.api.repository.PunishmentRepository;
import com.ddylan.hydrogen.api.repository.RankGrantRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerUtil {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PunishmentRepository punishmentRepository;

    public JSONObject getPlayerByUUID(String uuid, String username) {
        Player player = this.playerRepository.findByUuid(uuid);

        if (player == null) {
            player = new Player();
            player.setUuid(uuid);
            player.setUsername(username);
            player.setLastSeenAt(System.currentTimeMillis() / 1000L);

            this.playerRepository.save(player);
        }

        JSONObject json = player.toJSON();

        this.punishmentRepository.findByUuid(uuid).forEach(punishment -> {
            JSONObject access = new JSONObject();

            if (punishment.getRemovedAt() == 0L && punishment.getType().equals("BLACKLIST")) {
                access.put("allowed", Boolean.FALSE);

                access.put("message", HydrogenAPI.getSettingsManager().getSettings().get("blacklist-message"));
            } else if (activePunishment(punishment) && punishment.getType().equals("BAN")) {
                access.put("allowed", Boolean.FALSE);
                access.put("message", HydrogenAPI.getSettingsManager().getSettings().get("ban-message"));
            } else if (activePunishment(punishment) && punishment.getType().equals("MUTE")) {
                json.put("mute", punishment.toJSON());
            }
            if (!access.isEmpty()) {
                json.put("access", access);
            }
        });
        JSONObject scopeRanks = new JSONObject();
        List<String> ranks = new ArrayList<>();
        this.rankGrantRepository.findByUuid(uuid).forEach(rankGrant -> {
            if (rankGrant.getRemovedAt() == 0L) {
                scopeRanks.put(rankGrant.getRank(), rankGrant.getScopes());
                ranks.add(rankGrant.getRank());
            }
        });
        json.put("scopeRanks", scopeRanks);


        if (ranks.size() == 0) {
            ranks.add("default");
        }
        json.put("ranks", ranks);

        List<String> prefixes = new ArrayList<>();
        this.prefixGrantRepository.findByUuid(uuid).forEach(prefixGrant -> {
            if (prefixGrant.getRemovedAt() == 0L)
                prefixes.add(prefixGrant.getPrefix());
        });
        json.put("prefixes", prefixes);

        return json;
    }

    @Autowired
    private RankGrantRepository rankGrantRepository;
    @Autowired
    private PrefixGrantRepository prefixGrantRepository;

    public void logIp(String uuid, String ip) {
        Player player = this.playerRepository.findByUuid(uuid);

        if (!player.getIpLog().contains(ip)) {
            player.getIpLog().add(ip);
        }
        this.playerRepository.save(player);
    }

    public void setOnline(String uuid, String servername) {
        Player player = this.playerRepository.findByUuid(uuid);
        player.setOnline(true);
        player.setLastSeenOn(servername);
        this.playerRepository.save(player);
    }


    private boolean activePunishment(Punishment punishment) {
        if (punishment.getRemovedAt() != 0L) {
            return false;
        }

        if (punishment.getExpiresAt() == -1L) {
            return true;
        }

        return (System.currentTimeMillis() / 1000L < punishment.getExpiresAt());
    }

}
