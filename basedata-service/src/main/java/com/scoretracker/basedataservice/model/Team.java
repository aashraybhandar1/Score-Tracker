package com.scoretracker.basedataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value="team")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("id")
    private String footballApiId;
    private String name;
    private String code;
    private String leagueId;
    private String homeStadiumId;
    private String country;
    private int founded;
    private String logo;
}
