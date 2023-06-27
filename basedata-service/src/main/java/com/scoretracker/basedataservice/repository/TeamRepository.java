package com.scoretracker.basedataservice.repository;

import com.scoretracker.basedataservice.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team,String> {
}
