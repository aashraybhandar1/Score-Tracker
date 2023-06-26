package com.scoretracker.metadataservice.repository;

import com.scoretracker.metadataservice.model.League;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueRepository extends MongoRepository<League,String> {
}
