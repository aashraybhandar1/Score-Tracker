package com.scoretracker.fixtureservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scoretracker.fixtureservice.dto.FixtureByDate;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="t_fixture", schema = "fixture-service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fixture {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("id")
    private String footballApiId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String referee;
    private String timestamp;
    private String stadiumId;
    private String leagueId;
    private String homeTeamId;
    private String awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private String season;
    private String country;
    private String leagueName;
}
