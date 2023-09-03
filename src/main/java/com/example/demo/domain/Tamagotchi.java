package com.example.demo.domain;

import static com.example.demo.domain.Status.PENDING;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.example.demo.domain.exception.TamagotchiNameInvalidException;
import com.example.demo.domain.exception.TamagotchiStatusException;
import com.example.demo.dto.PocketDto.TamagotchiDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Setter(PRIVATE)
@Getter
class Tamagotchi {

  @Id
  @Getter
  private UUID id;

  private String name;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "pocket_id")
  private Pocket pocket;

  @Enumerated(STRING)
  private Status status;

  public void changeName(String name) {
    if (status == PENDING) {
      throw new TamagotchiStatusException("Tamagotchi cannot be modified because it's PENDING");
    }
    if (!nameIsValid(name)) {
      throw new TamagotchiNameInvalidException("Invalid Tamagotchi name: " + name);
    }
    this.name = name;
  }

  public void changeStatus(Status status) {
    this.status = status;
  }

  public TamagotchiDto toDto() {
    return new TamagotchiDto(id, name, status);
  }

  private static boolean nameIsValid(String name) {
    return name != null && !name.isBlank();
  }

  public static Tamagotchi newTamagotchi(String name, Status status, Pocket pocket) {
    Tamagotchi tamagotchi = new Tamagotchi();
    tamagotchi.setId(UUID.randomUUID());
    tamagotchi.setName(name);
    tamagotchi.setPocket(pocket);
    tamagotchi.setStatus(status);
    return tamagotchi;
  }
}
