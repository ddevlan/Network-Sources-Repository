package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.Prefix;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixRepository extends MongoRepository<Prefix, String> {}
