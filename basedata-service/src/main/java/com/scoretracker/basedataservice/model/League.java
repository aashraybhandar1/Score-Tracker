package com.scoretracker.basedataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value="league")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class League {
    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("id")
    private String footballApiID;
    @Indexed(unique = true)
    private String name;
    private String type;
    private String country;
    private String logo;
}
