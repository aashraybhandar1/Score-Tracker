package com.scoretracker.basedataservice.controller;

import com.scoretracker.basedataservice.service.BasedataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/fetchBasedata")
public class BasedataController {

    private final BasedataService basedataService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String fetchBaseData(){
        return basedataService.loadBaseData();
    }
}
