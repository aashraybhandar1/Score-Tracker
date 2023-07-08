package com.scoretracker.fixtureservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="t_fixture")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("id")
    private String footballApiId;
    private String referee;
    private String timestamp;
    private String stadiumId;
    private String leagueId;
    private String homeTeamId;
    private String awayTeamId;
    private String homeTeamName;
    private String getAwayTeamName;
    private String date;
    private String season;
}
