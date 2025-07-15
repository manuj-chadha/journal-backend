package com.journal.backend.repository;

import com.journal.backend.entity.Collection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepo extends MongoRepository<Collection, ObjectId> {
    boolean existsByUserAndTitle(ObjectId userId, String title);
    List<Collection> findByUser(ObjectId userId);

    @Query("{ '_id': ?0, 'user': ?1 }")
    Optional<Collection> findByIdAndUser(ObjectId id, ObjectId userId);


    Collection findCollectionByTitleAndUser(String unorganized, ObjectId id);
}