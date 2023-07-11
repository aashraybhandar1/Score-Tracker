package com.scoretracker.fixtureservice.service;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.model.Fixture;
import com.scoretracker.fixtureservice.repository.FixtureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureByDateService {
    private final FixtureRepository fixtureRepository;

    public String fetchFixturesByDate(Date date){
        ArrayList<Fixture> result = (ArrayList<Fixture>) fixtureRepository.getFixtureDataByDate(date);
        String temp = "";
        return temp;
    }
}
