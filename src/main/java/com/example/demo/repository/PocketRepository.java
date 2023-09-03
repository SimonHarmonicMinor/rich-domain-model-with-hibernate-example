package com.example.demo.repository;

import com.example.demo.domain.Pocket;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PocketRepository extends JpaRepository<Pocket, UUID> {

  @Query("SELECT p FROM Pocket p LEFT JOIN p.tamagotchis t WHERE t.id = :tamagotchiId")
  Optional<Pocket> findByTamagotchiId(UUID tamagotchiId);
}
