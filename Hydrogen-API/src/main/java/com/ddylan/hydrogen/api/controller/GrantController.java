package com.ddylan.hydrogen.api.controller;

import com.ddylan.hydrogen.api.model.RankGrant;
import com.ddylan.hydrogen.api.repository.PlayerRepository;
import com.ddylan.hydrogen.api.repository.RankGrantRepository;
import com.ddylan.hydrogen.api.util.ResponseUtil;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GrantController {
    @Autowired
    private RankGrantRepository rankGrantRepository;

    @GetMapping(path = {"/grants"})
    public ResponseEntity<List> getGrants(@RequestParam("user") String uuid) {
        List<JSONObject> grants = new ArrayList<>();
        this.rankGrantRepository.findByUuid(uuid).forEach(rankGrant -> grants.add(rankGrant.toJSON()));
        return new ResponseEntity(grants, HttpStatus.OK);
    }

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping(path = {"/grants"})
    public ResponseEntity<JSONObject> grant(@RequestBody Map<String, Object> body) {
        String user = body.get("user").toString();
        String reason = body.get("reason").toString();

        String addedBy = null;
        String addedByIp = null;
        if (body.containsKey("addedBy") && body.containsKey("addedByIp")) {
            addedBy = body.get("addedBy").toString();
            addedByIp = body.get("addedByIp").toString();
        }

        List<String> scopes = (List<String>) body.get("scopes");
        String rank = body.get("rank").toString();

        long expiresIn = -1L;
        if (body.containsKey("expiresIn")) {
            expiresIn = Long.valueOf(body.get("expiresIn").toString()).longValue();
        }
        this.rankGrantRepository.save(new RankGrant(user, reason, rank, scopes, expiresIn, System.currentTimeMillis() / 1000L, addedBy, addedByIp));

        return ResponseUtil.success;
    }

    @DeleteMapping(path = {"/grants/{grantid}"})
    public ResponseEntity<JSONObject> deleteGrant(@PathVariable("grantid") String grantId, @RequestBody Map<String, String> body) {
        String removedBy = body.get("removedBy");
        String removedByIp = body.get("removedByIp");
        String removalReason = body.get("reason");

        RankGrant grant = this.rankGrantRepository.findById(grantId).get();
        grant.setRemovedBy(removedBy);
        grant.setRemovedByIp(removedByIp);
        grant.setRemovalReason(removalReason);
        grant.setRemovedAt(System.currentTimeMillis() / 1000L);
        this.rankGrantRepository.save(grant);

        return ResponseUtil.success;
    }
}
