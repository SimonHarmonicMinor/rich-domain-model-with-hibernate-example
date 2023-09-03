package com.example.demo.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter(PRIVATE)
@Table(name = "deleted_tamagotchi")
class DeletedTamagotchi {
  @Id
  private UUID id;

  private String name;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "pocket_id")
  private Pocket pocket;

  @Enumerated(STRING)
  private Status status;

  public static DeletedTamagotchi newDeletedTamagotchi(Tamagotchi tamagotchi) {
    DeletedTamagotchi deletedTamagotchi = new DeletedTamagotchi();
    deletedTamagotchi.setId(UUID.randomUUID());
    deletedTamagotchi.setName(tamagotchi.getName());
    deletedTamagotchi.setPocket(tamagotchi.getPocket());
    deletedTamagotchi.setStatus(tamagotchi.getStatus());
    return deletedTamagotchi;
  }
}
