package com.scoretracker.fixtureservice.controller;

import com.scoretracker.fixtureservice.service.FixtureByDateService;
import com.scoretracker.fixtureservice.service.SeasonFixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/fixtureByDate")
@RequiredArgsConstructor
public class FixturesByDateController {
    private final FixtureByDateService fixtureByDateService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String fetchFixturesByDate(@RequestParam @DateTimeFormat(pattern="MMddyyyy") Date date){
        fixtureByDateService.fetchFixturesByDate(date);
        return "";
    }
}
