package com.scoretracker.fixtureservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoretracker.fixtureservice.model.Fixture;
import com.scoretracker.fixtureservice.repository.FixtureRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /*
        Cron job to fetch fixtures for the next 7 days. Looks to update the fixture database in case of some
        disruption.

        Does bulk updates for the fetched results.
     */

    @Scheduled(cron = "0 0 0 * * *")
    public void getUpdatedFixturesNextWeek() {
        List<String> leagueCodes = Arrays.asList(webClientBuilder.build().get()
                .uri("http://localhost:8080/api/fetchLeagueCodes")
                .retrieve()
                .bodyToMono(String[].class)
                .block());

        Map<String, Fixture> resultMap = new HashMap<String, Fixture>();

        IntStream.range(1,8).forEach(daysAhead -> {
            LocalDate day = LocalDate.now().plusDays(daysAhead);
            String response = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer
                                    .defaultCodecs()
                                    .maxInMemorySize(16 * 1024 * 1024))
                            .build())
                    .build().get().uri(rapidApiFixtureUrl + "date=" + day.toString())
                    .header("X-RapidAPI-Key", rapidApiKey)
                    .header("X-RapidAPI-Host", rapidApiHost)
                    .retrieve()
                    .bodyToMono(String.class).block();

            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                JsonNode fixturesResponse = jsonNode.get("response");

                List<String> updatedTimeStamps = new ArrayList<String>();
                List<String> footballApiIds = new ArrayList<String>();
                List<Date> updatedDates = new ArrayList<Date>();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
                fixturesResponse.forEach(fixtureResponse -> {
                    if(leagueCodes.contains(fixtureResponse.get("league").get("id"))){
                        try {
                            updatedTimeStamps.add(String.valueOf(fixtureResponse.get("fixture").get("timestamp")));
                            footballApiIds.add(String.valueOf(fixtureResponse.get("fixture").get("id")));

                            String date = String.valueOf((fixtureResponse.get("fixture").get("date")));
                            updatedDates.add(formatter.parse((date.substring(1,date.length()-1))));
                        }
                        catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                List<Object[]> results = fixtureRepository.getFixturesFromFixtureId(footballApiIds);
                Map<String, Fixture> resultMapAdd = results.stream()
                        .collect(Collectors.toMap(
                                result -> (String) result[0],
                                result -> (Fixture) result[1]
                        ));
                AtomicInteger counter = new AtomicInteger();
                footballApiIds.forEach(footballApiId -> {
                    Fixture fixture = resultMapAdd.getOrDefault(footballApiId, null);
                    if(fixture != null){
                        fixture.setDate(updatedDates.get(counter.get()));
                        fixture.setTimestamp(updatedTimeStamps.get(counter.get()));
                        resultMapAdd.put(footballApiId, fixture);
                    }
                    counter.getAndIncrement();
                });
                resultMap.putAll(resultMapAdd);

            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        fixtureRepository.saveAll(resultMap.values());
    }
}
