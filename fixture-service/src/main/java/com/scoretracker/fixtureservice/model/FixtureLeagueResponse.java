package com.scoretracker.fixtureservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixtureLeagueResponse {
    private String id;
    private String name;
    private String country;
    private String flag;
    private String round;
    private String season;
}
