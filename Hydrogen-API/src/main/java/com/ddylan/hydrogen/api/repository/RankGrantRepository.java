package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.RankGrant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankGrantRepository extends MongoRepository<RankGrant, String> {

    Optional<RankGrant> findById(String paramString);

    List<RankGrant> findByUuid(String paramString);
}
