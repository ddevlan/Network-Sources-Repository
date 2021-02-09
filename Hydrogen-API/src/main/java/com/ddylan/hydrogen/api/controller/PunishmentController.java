package com.ddylan.hydrogen.api.controller;

import com.ddylan.hydrogen.api.model.Punishment;
import com.ddylan.hydrogen.api.repository.PunishmentRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class PunishmentController {

    @GetMapping(path = {"/punishments"})
    public ResponseEntity<List> getPunishments(@RequestParam("user") String uuid) {
        List<JSONObject> punishments = new ArrayList<>();

        this.punishmentRepository.findByUuid(uuid).forEach(punishment -> punishments.add(punishment.toJSON()));



        return new ResponseEntity(punishments, HttpStatus.OK);
    }

    @Autowired
    private PunishmentRepository punishmentRepository;
    @PostMapping(path = {"/punishments"})
    public ResponseEntity<JSONObject> punish(@RequestHeader("MHQ-Authorization") String apiKey, @RequestBody Map<String, Object> body) {
        String player = body.get("user").toString();
        String playerIp = null;
        if (body.containsKey("userIp")) {
            playerIp = body.get("userIp").toString();
        }
        String addedBy = null;
        String addedByIp = null;
        if (body.containsKey("addedBy") && body.containsKey("addedByIp")) {
            addedBy = body.get("addedBy").toString();
            addedByIp = body.get("addedByIp").toString();
        }

        String publicReason = body.get("publicReason").toString();
        String privateReason = body.get("privateReason").toString();
        String punishType = body.get("type").toString();
        Map<String, String> metadata = (Map<String, String>)body.get("metadata");

        long expiresIn = -1L;
        if (body.containsKey("expiresIn")) {
            expiresIn = Long.parseLong(body.get("expiresIn").toString());
        }
        if (expiresIn != -1L) {
            expiresIn += System.currentTimeMillis() / 1000L;
        }
        this.punishmentRepository.save(new Punishment(player, playerIp, publicReason, privateReason, punishType, "Server", apiKey, addedBy, addedByIp,









                System.currentTimeMillis(), expiresIn, metadata));




        JSONObject response = new JSONObject();
        response.put("success", Boolean.valueOf(true));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping(path = {"/users/{uuid}/activePunishment"})
    public ResponseEntity<JSONObject> pardon(@PathVariable("uuid") String uuid, @RequestBody Map<String, String> body) {
        String type = body.get("type");
        String reason = body.get("reason");

        String removedBy = null;
        String removedByIp = null;
        if (body.containsKey("removedBy") && body.containsKey("removedByIp")) {
            removedBy = body.get("removedBy");
            removedByIp = body.get("removedByIp");
        }


        Punishment punishment = null;
        for (Punishment _punishment : this.punishmentRepository.findByUuid(uuid)) {
            if (_punishment.getType().equals(type) && _punishment
                    .getRemovedAt() == 0L && (_punishment
                    .getExpiresAt() == -1L || _punishment.getExpiresAt() * 1000L > System.currentTimeMillis()))
            {
                punishment = _punishment;
            }
        }

        JSONObject json = new JSONObject();

        if (punishment != null) {
            punishment.setRemovedBy(removedBy);
            punishment.setRemovedByIp(removedByIp);
            punishment.setRemovalReason(reason);
            punishment.setRemovedAt(System.currentTimeMillis());
            this.punishmentRepository.save(punishment);
            json.put("success", Boolean.valueOf(true));
        } else {
            json.put("success", Boolean.valueOf(false));
            json.put("message", "Player doesn't have an active punishment");
        }

        return new ResponseEntity(json, HttpStatus.OK);
    }
}
