package com.develhope.aquarium.repositories;

import com.develhope.aquarium.entities.Aquarium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AquariumRepository extends JpaRepository<Aquarium, Long> {
}
