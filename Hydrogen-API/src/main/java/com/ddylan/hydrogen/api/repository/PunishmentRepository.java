package com.ddylan.hydrogen.api.repository;

import com.ddylan.hydrogen.api.model.Punishment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PunishmentRepository extends MongoRepository<Punishment, String> {
    List<Punishment> findByUuid(String paramString);
}
