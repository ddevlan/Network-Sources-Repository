package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByUuid(String paramString);
}

