package com.scoretracker.fixtureservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoretracker.fixtureservice.repository.FixtureRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final FixtureRepository fixtureRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(fixedDelay = 120000)
    public void getUpdatedFixtures() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<String> leagueCodes = Arrays.asList(webClientBuilder.build().get()
                .uri("http://localhost:8080/api/fetchLeagueCodes")
                .retrieve()
                .bodyToMono(String[].class)
                .block());
        String response = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build().get().uri(rapidApiFixtureUrl + "date=" + tomorrow.toString())
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", rapidApiHost)
                .retrieve()
                .bodyToMono(String.class).block();

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode fixturesResponse = jsonNode.get("response");

            AtomicInteger count  = new AtomicInteger();
            fixturesResponse.forEach(fixtureResponse -> {
                if(!leagueCodes.contains(fixtureResponse.get("league").get("id"))){
                    count.getAndIncrement();
                    String updatedTimeStamp = String.valueOf(fixtureResponse.get("fixture").get("timestamp"));
                    String footballApiId = String.valueOf(fixtureResponse.get("fixture").get("id"));
                    Date updatedDate = new Date(String.valueOf((fixtureResponse.get("fixture").get("date"))));
                    String bleh = "ho";
                    fixtureRepository.updateTimestampByFootballApiId(updatedTimeStamp, updatedDate, footballApiId);
                    if(count.get()%5==0){
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
            });
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
