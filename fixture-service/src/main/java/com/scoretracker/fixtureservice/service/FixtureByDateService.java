package com.scoretracker.fixtureservice.service;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.model.Fixture;
import com.scoretracker.fixtureservice.repository.FixtureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureByDateService {
    private final FixtureRepository fixtureRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<FixtureByDate> fetchFixturesByDate(Date date){
        return modelMapper.map(fixtureRepository.getFixtureDataByDate(date), new TypeToken<List<FixtureByDate>>() {}.getType());

    }
}
