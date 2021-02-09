package com.ddylan.hydrogen.api.controller;

import com.ddylan.hydrogen.api.HydrogenAPI;
import com.ddylan.hydrogen.api.model.Player;
import com.ddylan.hydrogen.api.repository.PlayerRepository;
import com.ddylan.hydrogen.api.util.PlayerUtil;
import com.ddylan.hydrogen.api.util.ResponseUtil;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/users/{uuid}"})
public class UserController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public ResponseEntity<JSONObject> getPlayer(@PathVariable("uuid") String uuid) {
        Player player = this.playerRepository.findByUuid(uuid);
        JSONObject response = new JSONObject();

        if (player == null) {
            response.put("success", Boolean.FALSE);
            response.put("message", "Player hasn't joined the server before");
            return new ResponseEntity(response, HttpStatus.OK);
        }

        response.putAll(player.toJSON());
        response.put("lastUsername", player.getUsername());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Autowired
    private PlayerUtil playerUtil;

    @GetMapping(path = {"/details"})
    public ResponseEntity<JSONObject> getDetails(@PathVariable("uuid") String uuid) {
        Player player = this.playerRepository.findByUuid(uuid);
        JSONObject response = new JSONObject();

        if (player == null) {
            response.put("success", Boolean.FALSE);
            response.put("message", "Player hasn't joined the server before");
            return new ResponseEntity(response, HttpStatus.OK);
        }

        response.put("user", player.toJSON());
        response.put("ipLog", player.getIpLog());

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = {"/login"})
    public ResponseEntity<JSONObject> login(@RequestHeader("MHQ-Authorization") String apiKey, @PathVariable("uuid") String uuid, @RequestBody Map<String, String> body) {
        String username = body.get("username");
        String userIp = body.get("userIp");


        JSONObject response = this.playerUtil.getPlayerByUUID(uuid, username);

        this.playerUtil.setOnline(uuid, apiKey);

        if (HydrogenAPI.getSettingsManager().getSettings().get("log-ips").equals("true")) {
            this.playerUtil.logIp(uuid, userIp);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = {"/registerEmail"})
    public ResponseEntity<JSONObject> registerEmail(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String userIp = body.get("userIp");

        Player player = this.playerRepository.findByUuid(uuid);
        player.setEmail(email);
        this.playerRepository.save(player);

        return ResponseUtil.success;
    }

    @PostMapping(path = {"/prefix"})
    public ResponseEntity<JSONObject> updateActivePrefix(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> body) {
        String prefix = body.get("prefix");

        Player player = this.playerRepository.findByUuid(uuid);
        player.setActivePrefix(prefix);
        this.playerRepository.save(player);

        return ResponseUtil.success;
    }

    @PostMapping(path = {"/colors"})
    public ResponseEntity<JSONObject> updateColors(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> body) {
        String iconColor = body.get("iconColor");
        String nameColor = body.get("nameColor");

        Player player = this.playerRepository.findByUuid(uuid);
        player.setIconColor(iconColor);
        player.setNameColor(nameColor);
        this.playerRepository.save(player);

        return null;
    }

    @PostMapping(path = {"/verifyTotp"})
    public ResponseEntity<JSONObject> verifyTOTP(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> body) {
        String ip = body.get("userIp");
        String code = body.get("totpCode");

        JSONObject json = new JSONObject();
        json.put("authorized", Boolean.TRUE);
        json.put("message", "");
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @GetMapping(path = {"/requiresTotp"})
    public ResponseEntity<JSONObject> requiresTotp(@PathVariable("uuid") String uuid) {
        JSONObject json = new JSONObject();
        json.put("required", Boolean.FALSE);
        json.put("message", "NOT_REQUIRED_IP_PRE_AUTHORIZED");
        return new ResponseEntity(json, HttpStatus.OK);
    }
}
