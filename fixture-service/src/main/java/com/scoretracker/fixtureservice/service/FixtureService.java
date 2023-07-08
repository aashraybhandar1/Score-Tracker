package com.scoretracker.fixtureservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoretracker.fixtureservice.model.Fixture;
import com.scoretracker.fixtureservice.repository.FixtureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureService {
    private final FixtureRepository fixtureRepository;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${rapidApiKey}")
    private String rapidApiKey;

    @Value("${rapidApiHost}")
    private String rapidApiHost;

    @Value("${rapidApiFixtureUrl}")
    private String rapidApiFixtureUrl;


    public String [] fetchSeasonFixture(String seasonYear) {
        String [] leagueCodes = webClientBuilder.build().get()
                .uri("http://localhost:8080/api/fetchLeagueCodes")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        /* Improve the web Client configuration mechanism so as to have pre built configuration and not the Builder bean*/
        Arrays.stream(leagueCodes).forEach(leagueCode -> {
            String hello = ""+rapidApiFixtureUrl+"league="+leagueCode+"season="+seasonYear;
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
                JsonNode fixtures = jsonNode.get("response");
                fixtures.forEach(fixture -> {
                    try {
                        JsonNode bleh = fixture.get("fixture");
                        Fixture fixture1 = objectMapper.treeToValue(bleh, Fixture.class);
                        fixture1.setLeagueId(String.valueOf(fixture.get("league").get("id")));
                        fixture1.setSeason(String.valueOf(fixture.get("league").get("season")));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    String temp3 = "";
                });
                String temp2 = "";
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String temp = "";
        });
        return leagueCodes;
    }
}
