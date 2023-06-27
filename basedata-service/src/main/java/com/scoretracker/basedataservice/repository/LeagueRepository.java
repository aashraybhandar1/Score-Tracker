package com.scoretracker.basedataservice.repository;

import com.scoretracker.basedataservice.model.League;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueRepository extends MongoRepository<League,String> {
}
