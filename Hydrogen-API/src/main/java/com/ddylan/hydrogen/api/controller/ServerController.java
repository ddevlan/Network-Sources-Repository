package com.ddylan.hydrogen.api.controller;

import com.ddylan.hydrogen.api.HydrogenAPI;
import com.ddylan.hydrogen.api.model.Heartbeat;
import com.ddylan.hydrogen.api.model.Player;
import com.ddylan.hydrogen.api.repository.*;
import com.ddylan.hydrogen.api.util.PlayerUtil;
import com.ddylan.hydrogen.api.util.RankUtil;
import com.ddylan.hydrogen.api.util.ServerUtil;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ServerController {

    @Autowired
    private ServerGroupRepository serverGroupRepository;
    @Autowired
    private ChatFilterRepository chatFilterRepository;
    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @GetMapping(path = {"/serverGroups"})
    public ResponseEntity<List<JSONObject>> getServerGroups() {
        List<JSONObject> serverGroups = new ArrayList<>();
        this.serverGroupRepository.findAll().forEach(serverGroup -> serverGroups.add(serverGroup.toJSON()));
        return new ResponseEntity<List<JSONObject>>(serverGroups, HttpStatus.OK);
    }

    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerUtil playerUtil;
    @Autowired
    private RankUtil rankUtil;

    @GetMapping(path = {"/servers"})
    public ResponseEntity<Set<JSONObject>> getServers() {
        return new ResponseEntity<>(ServerUtil.getServersAsJSON(), HttpStatus.OK);
    }


    @PostMapping(path = {"/heartbeat"})
    public ResponseEntity<JSONObject> serverHeartbeat(@RequestHeader("MHQ-Authorization") String apiKey, @RequestBody Map<String, Object> body) {
        ServerUtil.update(apiKey, body);

        Map<String, Map> onlinePlayers = (Map<String, Map>) body.get("players");

        JSONObject players = new JSONObject();

        onlinePlayers.entrySet().forEach(entry -> {
            players.put(entry.getKey(), this.playerUtil.getPlayerByUUID(entry.getKey(), null));

            Player player = this.playerRepository.findByUuid(entry.getKey());

            player.setOnline(true);
            player.setLastSeenOn(apiKey);
            this.playerRepository.save(player);
            this.playerUtil.setOnline(entry.getKey(), apiKey);
        });
        List<Map<String, String>> events = (List<Map<String, String>>) body.get("events");
        events.forEach(event -> {
            if (event.get("type").equals("leave")) {
                Player player = this.playerRepository.findByUuid(event.get("user"));

                player.setOnline(false);
                this.playerRepository.save(player);
            }
        });
        boolean permissionsNeeded = true;
        JSONObject rankPermissions = new JSONObject();
        if (permissionsNeeded) {
            this.rankRepository.findAll().forEach(rank -> rankPermissions.put(rank.getRankid(), rank.getPermissions()));
        }

        JSONObject response = new JSONObject();
        response.put("players", players);
        response.put("permissions", rankPermissions);

        this.heartbeatRepository.save(new Heartbeat(players, events, 0.0D, permissionsNeeded));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = {"/chatFilter"})
    public ResponseEntity<Set> getChatFilters() {
        return new ResponseEntity<>(new HashSet(this.chatFilterRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping(path = {"/whoami"})
    public ResponseEntity<JSONObject> getWhoAmI(@RequestHeader("MHQ-Authorization") String apiKey) {
        JSONObject response = new JSONObject();
        response.put("name", apiKey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = {"/dumps/totp"})
    public ResponseEntity<Set> getTotpDumps() {
        return new ResponseEntity<>(new HashSet(), HttpStatus.OK);
    }

    @PostMapping(path = {"/users/disposableLoginTokens"})
    public ResponseEntity<JSONObject> getDisposableToken(@RequestBody Map<String, String> body) {
        String uuid = body.get("user");
        String ip = body.get("userIp");

        JSONObject json = new JSONObject();

        Player player = this.playerRepository.findByUuid(uuid);
        if (player.getEmail() == null) {
            json.put("success", Boolean.valueOf(false));
            json.put("message", "Your profile doesn't have an account.");
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        String token = UUID.randomUUID().toString();


        HydrogenAPI.getRedisManager().getJedis().hset("disposableTokens", token, uuid);
        json.put("token", token);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
