package com.scoretracker.basedataservice.controller;

import com.scoretracker.basedataservice.service.LeagueCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/fetchLeagueCodes")
public class LeagueCodeController {

    private final LeagueCodeService leagueCodeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<String> fetchLeagueCode(){
        return leagueCodeService.fetchLeagueCode();
    }
}
