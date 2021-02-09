package com.ddylan.hydrogen.api.controller;

import com.ddylan.hydrogen.api.model.PrefixGrant;
import com.ddylan.hydrogen.api.repository.PlayerRepository;
import com.ddylan.hydrogen.api.repository.PrefixGrantRepository;
import com.ddylan.hydrogen.api.repository.PrefixRepository;
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
public class PrefixController {
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping(path = {"/prefixes"})
    public ResponseEntity<List> getPrefixes() {
        List<JSONObject> prefixes = new ArrayList<>();

        this.prefixRepository.findAll().forEach(prefix -> prefixes.add(prefix.toJSON()));

        return new ResponseEntity(prefixes, HttpStatus.OK);
    }

    @Autowired
    private PrefixRepository prefixRepository;

    @Autowired
    private PrefixGrantRepository prefixGrantRepository;

    @PostMapping(path = {"/prefixes"})
    public ResponseEntity<JSONObject> grantPrefix(@RequestBody Map<String, Object> body) {
        String user = body.get("user").toString();
        String reason = body.get("reason").toString();
        String prefix = body.get("prefix").toString();
        List<String> scopes = (List<String>) body.get("scopes");

        String addedBy = null;
        String addedByIp = null;
        if (body.containsKey("addedBy") && body.containsKey("addedByIp")) {
            addedBy = body.get("addedBy").toString();
            addedByIp = body.get("addedByIp").toString();
        }

        long expiresIn = -1L;
        if (body.containsKey("expiresIn")) {
            expiresIn = Long.parseLong(body.get("expiresIn").toString());
        }
        this.prefixGrantRepository.save(new PrefixGrant(user, reason, prefix, scopes, expiresIn, System.currentTimeMillis() / 1000L, addedBy, addedByIp));

        return ResponseUtil.success;
    }

    @GetMapping(path = {"/prefixes/grants"})
    public ResponseEntity<List> getPrefixGrants(@RequestParam("user") String uuid) {
        List<JSONObject> grants = new ArrayList<>();

        this.prefixGrantRepository.findByUuid(uuid).forEach(prefixGrant -> grants.add(prefixGrant.toJSON()));


        return new ResponseEntity(grants, HttpStatus.OK);
    }

    @DeleteMapping(path = {"/prefixes/{prefixid}"})
    public ResponseEntity<JSONObject> deletePrefixGrant(@PathVariable("prefixid") String prefixid, @RequestBody Map<String, String> body) {
        String removedBy = body.get("removedBy");
        String removedByIp = body.get("removedByIp");
        String removalReason = body.get("reason");

        PrefixGrant grant = this.prefixGrantRepository.findById(prefixid).get();
        grant.setRemovedBy(removedBy);
        grant.setRemovedByIp(removedByIp);
        grant.setRemovalReason(removalReason);
        grant.setRemovedAt(System.currentTimeMillis() / 1000L);
        this.prefixGrantRepository.save(grant);

        return ResponseUtil.success;
    }
}
