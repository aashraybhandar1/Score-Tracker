package com.scoretracker.fixtureservice.repository;

import com.scoretracker.fixtureservice.model.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
}
