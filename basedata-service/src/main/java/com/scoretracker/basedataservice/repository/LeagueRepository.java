package com.scoretracker.basedataservice.repository;

import com.scoretracker.basedataservice.model.League;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface LeagueRepository extends MongoRepository<League,String> {
}
