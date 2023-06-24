package com.scoretracker.metadataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Stadium {
    @Id
    private String id;
    private String footballApiId;
    private String name;
    private String city;
    private int capacity;
}
