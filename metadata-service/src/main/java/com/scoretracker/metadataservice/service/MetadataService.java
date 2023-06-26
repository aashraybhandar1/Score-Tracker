package com.scoretracker.metadataservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class MetadataService {
    private final WebClient.Builder webClientBuilder;
    @Value("${rapidApiKey}")
    private String rapidApiKey;

    @Value("${rapidApiHost}")
    private String rapidApiHost;

    @Value("${rapidApiUrl}")
    private String rapidApiUrl;

    public String loadBaseData(){
        String response = webClientBuilder.build().get().uri(rapidApiUrl)
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", rapidApiHost)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(response);
        return response;
    }
}
