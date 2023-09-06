package com.example.demo.service;

import com.example.demo.domain.Pocket;
import com.example.demo.domain.command.TamagotchiCreateRequest;
import com.example.demo.domain.command.TamagotchiUpdateRequest;
import com.example.demo.domain.exception.TamagotchiNameInvalidException;
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
    boolean nameIsNotUnique = em.createQuery(
            """
                SELECT COUNT(t) > 0 FROM Tamagotchi t
                WHERE t.id <> :tamagotchiId AND t.name = :newName
                """,
            boolean.class
        ).setParameter("tamagotchiId", tamagotchiId)
        .setParameter("newName", request.name())
        .getSingleResult();

    if (nameIsNotUnique) {
      throw new TamagotchiNameInvalidException("Tamagotchi name is not unique: " + request.name());
    }

    UUID pocketId = em.createQuery(
            "SELECT t.pocket.id AS id FROM Tamagotchi t WHERE t.id = :tamagotchiId",
            UUID.class
        )
        .setParameter("tamagotchiId", tamagotchiId)
        .getSingleResult();

    Pocket pocket = em.find(Pocket.class, pocketId);
    pocket.updateTamagotchi(tamagotchiId, request);
  }
}
