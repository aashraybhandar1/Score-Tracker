package com.scoretracker.fixtureservice.controller;

import com.scoretracker.fixtureservice.service.FixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seasonFixture")
@RequiredArgsConstructor
public class FixtureController {
    private final FixtureService fixtureService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String fetchSeasonFixture(@RequestParam String seasonYear){
         return fixtureService.fetchSeasonFixture(seasonYear);
    }
}
