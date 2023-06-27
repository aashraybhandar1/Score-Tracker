package com.scoretracker.basedataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Team {
    @Id
    private String id;
    private String footballApiId;
    private String name;
    private String code;
    private String leagueId;
    private String homeStadiumId;
    private String country;
    private int founded;
}
