package com.scoretracker.basedataservice.service;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.scoretracker.basedataservice.model.League;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class LeagueCodeService {
    private final MongoTemplate mongoTemplate;

    public Set<String> fetchLeagueCode(){
        DistinctIterable<String> temp = mongoTemplate.getCollection("league").distinct("footballApiID", String.class);
        String abc = "vhbj";
        return null;
    }
}
