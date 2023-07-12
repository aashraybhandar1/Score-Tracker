package com.scoretracker.fixtureservice.repository;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.model.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    @Query(value = "Select * From t_fixture Where Date(date) = Date(:date)",
            nativeQuery = true)
    List<Fixture> getFixtureDataByDate(@Param("date") Date date);
}
