package com.scoretracker.basedataservice.service;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.scoretracker.basedataservice.model.League;
import com.scoretracker.basedataservice.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LeagueCodeService {
    private final MongoTemplate mongoTemplate;

    public List<String> fetchLeagueCode(){
        Query query = new Query();
        query.fields().include("footballApiID");
        query.addCriteria(Criteria.where("footballApiID").exists(true));
        return mongoTemplate.findDistinct(query,"footballApiID",League.class, String.class);
    }
}
