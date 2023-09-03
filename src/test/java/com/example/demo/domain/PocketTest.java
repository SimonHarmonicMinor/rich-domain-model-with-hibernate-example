package com.example.demo.domain;

import static com.example.demo.domain.Status.CREATED;
import static com.example.demo.domain.Status.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.demo.domain.command.TamagotchiCreateRequest;
import com.example.demo.domain.exception.TamagotchiDeleteException;
import com.example.demo.dto.PocketDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PocketTest {

  @Test
  void shouldCreatePocketWithTamagotchi() {
    Pocket pocket = Pocket.newPocket("My pocket");

    PocketDto dto = pocket.toDto();
    assertEquals(1, dto.tamagotchis().size());
  }

  @Test
  void shouldForbidDeletionOfASingleTamagotchi() {
    Pocket pocket = Pocket.newPocket("My pocket");
    PocketDto dto = pocket.toDto();
    UUID tamagotchiId = dto.tamagotchis().get(0).id();

    assertThrows(
        TamagotchiDeleteException.class,
        () -> pocket.deleteTamagotchi(tamagotchiId)
    );
  }

  @Test
  void shouldDeleteTamagotchiById() {
    Pocket pocket = Pocket.newPocket("My pocket");
    UUID tamagotchiId = pocket.createTamagotchi(new TamagotchiCreateRequest("My tamagotchi", CREATED));

    pocket.deleteTamagotchi(tamagotchiId);

    PocketDto dto = pocket.toDto();
    assertThat(dto.tamagotchis())
        .noneMatch(t -> t.name().equals("My tamagotchi"));
  }

  @Test
  void shouldRestoreTamagotchiById() {
    Pocket pocket = Pocket.newPocket("My pocket");
    UUID tamagotchiId = pocket.createTamagotchi(new TamagotchiCreateRequest("My tamagotchi", CREATED));
    pocket.deleteTamagotchi(tamagotchiId);

    pocket.restoreTamagotchi("My tamagotchi");

    PocketDto dto = pocket.toDto();
    assertThat(dto.tamagotchis())
        .anyMatch(t -> t.name().equals("My tamagotchi"));
  }

  @Test
  void shouldRestoreTheLastTamagotchi() {
    Pocket pocket = Pocket.newPocket("My pocket");
    UUID tamagotchiId = pocket.createTamagotchi(new TamagotchiCreateRequest("My tamagotchi", CREATED));
    pocket.deleteTamagotchi(tamagotchiId);
    tamagotchiId = pocket.createTamagotchi(new TamagotchiCreateRequest("My tamagotchi", PENDING));
    pocket.deleteTamagotchi(tamagotchiId);

    pocket.restoreTamagotchi("My tamagotchi");

    PocketDto dto = pocket.toDto();
    assertThat(dto.tamagotchis())
        .anyMatch(t ->
            t.name().equals("My tamagotchi")
            && t.status().equals(PENDING)
        );
  }
}