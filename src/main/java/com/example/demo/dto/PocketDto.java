package com.example.demo.dto;

import com.example.demo.domain.Status;
import java.util.List;
import java.util.UUID;

public record PocketDto(
    UUID id,
    String name,
    List<TamagotchiDto> tamagotchis
) {

  public record TamagotchiDto(
      UUID id,
      String name,
      Status status
  ) {

  }
}
