package com.example.demo.service;

import com.example.demo.domain.Pocket;
import com.example.demo.domain.command.TamagotchiCreateRequest;
import com.example.demo.domain.command.TamagotchiUpdateRequest;
import com.example.demo.repository.PocketRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PocketService {

  private final PocketRepository pocketRepository;

  @Transactional
  public UUID createPocket(String name) {
    return pocketRepository.save(
        Pocket.newPocket(name)
    ).getId();
  }

  @Transactional
  public UUID createTamagotchi(UUID pocketId, TamagotchiCreateRequest request) {
    Pocket pocket = pocketRepository.findById(pocketId).orElseThrow();
    return pocket.createTamagotchi(request);
  }

  @Transactional
  public void updateTamagotchi(UUID tamagotchiId, TamagotchiUpdateRequest request) {
    Pocket pocket = pocketRepository.findByTamagotchiId(tamagotchiId).orElseThrow();
    pocket.updateTamagotchi(tamagotchiId, request);
  }
}
