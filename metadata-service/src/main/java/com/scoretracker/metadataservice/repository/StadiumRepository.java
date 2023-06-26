package com.scoretracker.metadataservice.repository;

import com.scoretracker.metadataservice.model.Stadium;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StadiumRepository extends MongoRepository<Stadium,String> {
}
