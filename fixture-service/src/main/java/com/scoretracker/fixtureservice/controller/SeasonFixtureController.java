package com.scoretracker.fixtureservice.controller;

import com.scoretracker.fixtureservice.service.SeasonFixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seasonFixture")
@RequiredArgsConstructor
public class SeasonFixtureController {
    private final SeasonFixtureService seasonFixtureService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String fetchSeasonFixture(@RequestParam String seasonYear){
         return seasonFixtureService.fetchSeasonFixture(seasonYear);
    }
}
