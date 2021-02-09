package com.ddylan.hydrogen.api.util;

import com.ddylan.hydrogen.api.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankUtil {
    @Autowired
    private RankRepository rankRepository;

    public List<String> getRankPermissions(String rank) {
        return this.rankRepository.findByRankid(rank).getPermissions();
    }
}
