package com.scoretracker.fixtureservice.repository;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.model.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    @Query(value = "Select football_api_id, home_team_name, away_team_name, date, league_name, country" +
            "From t_fixture Where date= :date",
            nativeQuery = true)
    FixtureByDate getFixtureDataByDate(@Param("date") Date date);
}
