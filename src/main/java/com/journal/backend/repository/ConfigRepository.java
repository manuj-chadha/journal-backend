package com.journal.backend.repository;

import com.journal.backend.entity.ConfigKey;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<ConfigKey, ObjectId> {

}