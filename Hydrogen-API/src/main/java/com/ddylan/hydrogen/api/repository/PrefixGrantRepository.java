package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.PrefixGrant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface PrefixGrantRepository extends MongoRepository<PrefixGrant, String> {

    Optional<PrefixGrant> findById(String paramString);

    List<PrefixGrant> findByUuid(String paramString);
}
