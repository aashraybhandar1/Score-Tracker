package com.scoretracker.metadataservice.controller;

import com.scoretracker.metadataservice.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/fetchMetadata")
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllProducts(){
        return metadataService.loadBaseData();
    }
}
