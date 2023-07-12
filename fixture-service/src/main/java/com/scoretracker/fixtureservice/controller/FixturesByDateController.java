package com.scoretracker.fixtureservice.controller;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.service.FixtureByDateService;
import com.scoretracker.fixtureservice.service.SeasonFixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/fixtureByDate")
@RequiredArgsConstructor
public class FixturesByDateController {
    private final FixtureByDateService fixtureByDateService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FixtureByDate> fetchFixturesByDate(@RequestParam @DateTimeFormat(pattern="MMddyyyy") Date date){
        return fixtureByDateService.fetchFixturesByDate(date);
    }
}
