package com.scoretracker.basedataservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.scoretracker.basedataservice.constants.Constants;
import com.scoretracker.basedataservice.model.League;
import com.scoretracker.basedataservice.model.Stadium;
import com.scoretracker.basedataservice.model.Team;
import com.scoretracker.basedataservice.repository.LeagueRepository;
import com.scoretracker.basedataservice.repository.StadiumRepository;
import com.scoretracker.basedataservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class BasedataService {
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    private final LeagueRepository leagueRepository;
    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;
    @Value("${rapidApiKey}")
    private String rapidApiKey;

    @Value("${rapidApiHost}")
    private String rapidApiHost;

    @Value("${rapidApiLeagueUrl}")
    private String rapidApiLeagueUrl;

    @Value("${rapidApiTeamUrl}")
    private String rapidApiTeamUrl;

    public String loadBaseData(){
        List<String> leagueCodes = loadLeagueData();
        loadTeamandStadiumData(leagueCodes);
        return "Basedata loaded successfully";
    }

    public ArrayList<String> loadLeagueData (){
        ArrayList<String> leagueCodes = new ArrayList<String>();
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
                    leagueCodes.add(league.getFootballApiID());
                    leagueRepository.save(league);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
        });
        return leagueCodes;
    }

    public void loadTeamandStadiumData(List<String> leagueCodes){
        leagueCodes.forEach(code -> {
            String response = webClientBuilder.build().get().uri(rapidApiTeamUrl+""+code)
                    .header("X-RapidAPI-Key", rapidApiKey)
                    .header("X-RapidAPI-Host", rapidApiHost)
                    .retrieve()
                    .bodyToMono(String.class).block();
            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                jsonNode.get("response").forEach(responseNode -> {
                    try {
                        Team team = objectMapper.treeToValue(responseNode.get("team"), Team.class);
                        Stadium stadium = objectMapper.treeToValue(responseNode.get("venue"), Stadium.class);
                        team.setLeagueId(code);
                        team.setHomeStadiumId(stadium.getFootballApiId());
                        teamRepository.save(team);
                        stadiumRepository.save(stadium);
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
    }
}
