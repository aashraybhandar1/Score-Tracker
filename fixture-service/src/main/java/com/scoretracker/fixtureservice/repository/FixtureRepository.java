package com.scoretracker.fixtureservice.repository;

import com.scoretracker.fixtureservice.dto.FixtureByDate;
import com.scoretracker.fixtureservice.model.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    @Query(value = "Select * From t_fixture Where Date(date) = Date(:date)",
            nativeQuery = true)
    List<Fixture> getFixtureDataByDate(@Param("date") Date date);

    @Transactional
    @Modifying
    @Query(value="Select f.footballApiId, f FROM Fixture f WHERE f.footballApiId in :footballApiId")
    List<Object[]> getFixturesFromFixtureId(@Param("footballApiId") List<String> footballApiId);

    @Transactional
    @Modifying
    @Query(value="UPDATE t_fixture f SET f.timestamp = :timestamp WHERE f.football_api_id = :footballApiId",
            nativeQuery = true)
    void updateTimestampByFootballApiId(@Param("timestamp") String timestamp, @Param("footballApiId") String footballApiId);
}
