package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.ServerGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerGroupRepository extends MongoRepository<ServerGroup, String> {}
