package com.scoretracker.fixtureservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoretracker.fixtureservice.dto.FixtureLeagueResponse;
import com.scoretracker.fixtureservice.dto.FixtureTeamResponse;
import com.scoretracker.fixtureservice.model.Fixture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FixtureUpdateService {
    @Value("${rapidApiKey}")
    private String rapidApiKey;

    @Value("${rapidApiHost}")
    private String rapidApiHost;

    @Value("${rapidApiFixtureUrl}")
    private String rapidApiFixtureUrl;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    @Scheduled(fixedRate = 60000)
    public void getUpdatedFixtures(){
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        String [] leagueCodes = webClientBuilder.build().get()
                .uri("http://localhost:8080/api/fetchLeagueCodes")
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        String response = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build().get().uri(rapidApiFixtureUrl+"date="+tomorrow.toString())
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", rapidApiHost)
                .retrieve()
                .bodyToMono(String.class).block();
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
    }
}
