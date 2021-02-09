package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.ChatFilter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatFilterRepository extends MongoRepository<ChatFilter, String> {}
