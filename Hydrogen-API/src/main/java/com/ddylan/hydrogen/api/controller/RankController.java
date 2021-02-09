package com.ddylan.hydrogen.api.controller;

import com.ddylan.hydrogen.api.repository.RankRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = {"/ranks"})
public class RankController {

    @GetMapping
    public ResponseEntity<List> getRanks() {
        List<JSONObject> ranks = new ArrayList<>();

        this.rankRepository.findAll().forEach(rank -> ranks.add(rank.toJSON()));

        return new ResponseEntity(ranks, HttpStatus.OK);
    }

    @Autowired
    private RankRepository rankRepository;
}
