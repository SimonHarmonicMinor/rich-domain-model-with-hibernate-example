package com.example.demo.service;

import com.example.demo.domain.Pocket;
import com.example.demo.domain.command.TamagotchiCreateRequest;
import com.example.demo.domain.command.TamagotchiUpdateRequest;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PocketService {

  private final EntityManager em;

  @Transactional
  public UUID createPocket(String name) {
    Pocket pocket = Pocket.newPocket(name);
    em.persist(pocket);
    return pocket.getId();
  }

  @Transactional
  public UUID createTamagotchi(UUID pocketId, TamagotchiCreateRequest request) {
    Pocket pocket = em.find(Pocket.class, pocketId);
    return pocket.createTamagotchi(request);
  }

  @Transactional
  public void updateTamagotchi(UUID tamagotchiId, TamagotchiUpdateRequest request) {
    Pocket pocket = em.createQuery(
            "SELECT p FROM Pocket p LEFT JOIN p.tamagotchis t WHERE t.id = :tamagotchiId",
            Pocket.class
        )
        .setParameter("tamagotchiId", tamagotchiId)
        .getSingleResult();
    pocket.updateTamagotchi(tamagotchiId, request);
  }
}
