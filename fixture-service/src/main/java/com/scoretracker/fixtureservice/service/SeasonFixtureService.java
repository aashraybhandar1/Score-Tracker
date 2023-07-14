package com.scoretracker.fixtureservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoretracker.fixtureservice.model.Fixture;
import com.scoretracker.fixtureservice.dto.FixtureLeagueResponse;
import com.scoretracker.fixtureservice.dto.FixtureTeamResponse;
import com.scoretracker.fixtureservice.repository.FixtureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

/*
 TO DO:
 1) Introduce logging for tracking if request performs as expected
 2) Improve response for this service

 */

@Service
@RequiredArgsConstructor
@Transactional
public class SeasonFixtureService {
    private final FixtureRepository fixtureRepository;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${rapidApiKey}")
    private String rapidApiKey;

    @Value("${rapidApiHost}")
    private String rapidApiHost;

    @Value("${rapidApiFixtureUrl}")
    private String rapidApiFixtureUrl;


    public String fetchSeasonFixture(String seasonYear) {
        String [] leagueCodes = webClientBuilder.build().get()
                .uri("http://localhost:8080/api/fetchLeagueCodes")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        /* Improve the web Client configuration mechanism so as to have pre built configuration and not the Builder bean*/
        Arrays.stream(leagueCodes).forEach(leagueCode -> {
            String response = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                    .build())
                    .build().get().uri(rapidApiFixtureUrl+"league="+leagueCode+"&season="+seasonYear)
                    .header("X-RapidAPI-Key", rapidApiKey)
                    .header("X-RapidAPI-Host", rapidApiHost)
                    .retrieve()
                    .bodyToMono(String.class).block();
            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                JsonNode fixturesResponse = jsonNode.get("response");
                fixturesResponse.forEach(fixtureResponse -> {
                    try {
                        Fixture fixture = objectMapper.treeToValue(fixtureResponse.get("fixture"), Fixture.class);
                        FixtureTeamResponse homeTeamResponse = objectMapper.treeToValue(fixtureResponse.get("teams").get("home"), FixtureTeamResponse.class);
                        FixtureTeamResponse awayTeamResponse = objectMapper.treeToValue(fixtureResponse.get("teams").get("away"), FixtureTeamResponse.class);
                        FixtureLeagueResponse fixtureLeagueResponse = objectMapper.treeToValue(fixtureResponse.get("league"),FixtureLeagueResponse.class);

                        //Set league related details from response
                        fixture.setLeagueId(fixtureLeagueResponse.getId());
                        fixture.setCountry(fixtureLeagueResponse.getCountry());
                        fixture.setLeagueName(fixtureLeagueResponse.getName());
                        fixture.setSeason(fixtureLeagueResponse.getSeason());

                        //Set team related details from response
                        fixture.setAwayTeamId(awayTeamResponse.getId());
                        fixture.setHomeTeamId(homeTeamResponse.getId());
                        fixture.setAwayTeamName(awayTeamResponse.getName());
                        fixture.setHomeTeamName(homeTeamResponse.getName());

                        fixtureRepository.save(fixture);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return "Fixture data successfully populated in DB";
    }
}
