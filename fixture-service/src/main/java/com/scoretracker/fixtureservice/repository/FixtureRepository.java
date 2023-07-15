package com.scoretracker.fixtureservice.repository;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.model.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    @Query(value = "Select * From t_fixture Where Date(date) = Date(:date)",
            nativeQuery = true)
    List<Fixture> getFixtureDataByDate(@Param("date") Date date);

    @Transactional
    @Modifying
    @Query("UPDATE t_fixture f SET f.timestamp = :timestamp AND f.DATE = :date WHERE f.footballApiId = :footballApiId")
    void updateTimestampByFootballApiId(@Param("timestamp") String timestamp, @Param("date") Date date, @Param("footballApiId") String footballApiId);
}
