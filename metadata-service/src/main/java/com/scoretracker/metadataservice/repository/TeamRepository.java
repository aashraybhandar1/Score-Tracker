package com.scoretracker.metadataservice.repository;

import com.scoretracker.metadataservice.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team,String> {
}
