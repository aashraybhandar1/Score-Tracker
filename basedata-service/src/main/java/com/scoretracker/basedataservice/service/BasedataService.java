package com.scoretracker.basedataservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoretracker.basedataservice.constants.Constants;
import com.scoretracker.basedataservice.model.League;
import com.scoretracker.basedataservice.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class BasedataService {
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    private final LeagueRepository leagueRepository;
    @Value("${rapidApiKey}")
    private String rapidApiKey;

    @Value("${rapidApiHost}")
    private String rapidApiHost;

    @Value("${rapidApiUrl}")
    private String rapidApiUrl;

    @Value("${rapidApiLeagueUrl}")
    private String rapidApiLeagueUrl;

    public String loadBaseData(){
        loadLeagueData();
        return "Basedata loaded successfully";
    }

    public void loadLeagueData (){
        Arrays.stream(Constants.counties).forEach(country -> {
             String response = webClientBuilder.build().get().uri(rapidApiLeagueUrl+""+country)
                    .header("X-RapidAPI-Key", rapidApiKey)
                    .header("X-RapidAPI-Host", rapidApiHost)
                    .retrieve()
                    .bodyToMono(String.class).block();
             try {
                    JsonNode jsonNode = objectMapper.readTree(response);
                    JsonNode fieldOfInterest = jsonNode.get("response").get(0).get("league");
                    League league = objectMapper.treeToValue(fieldOfInterest, League.class);
                    league.setCountry(country);
                    leagueRepository.save(league);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
        });
    }
}
