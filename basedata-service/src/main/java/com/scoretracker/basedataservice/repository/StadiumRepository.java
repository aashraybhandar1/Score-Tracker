package com.scoretracker.basedataservice.repository;

import com.scoretracker.basedataservice.model.Stadium;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StadiumRepository extends MongoRepository<Stadium,String> {
}
