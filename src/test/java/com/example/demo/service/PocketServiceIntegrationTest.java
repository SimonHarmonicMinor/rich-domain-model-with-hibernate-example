package com.example.demo.service;

import static com.example.demo.domain.Status.CREATED;
import static com.example.demo.domain.Status.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.example.demo.domain.Pocket;
import com.example.demo.domain.command.TamagotchiCreateRequest;
import com.example.demo.domain.command.TamagotchiUpdateRequest;
import com.example.demo.domain.exception.TamagotchiNameInvalidException;
import com.example.demo.dto.PocketDto;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Transactional(propagation = NOT_SUPPORTED)
@Import(PocketService.class)
class PocketServiceIntegrationTest {

  @Container
  @ServiceConnection
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  @Autowired
  private TransactionTemplate transactionTemplate;
  @Autowired
  private TestEntityManager em;
  @Autowired
  private PocketService pocketService;

  @BeforeEach
  void cleanDatabase() {
    transactionTemplate.executeWithoutResult(
        s -> em.getEntityManager().createQuery("DELETE FROM Pocket ").executeUpdate()
    );
  }

  @Test
  void shouldCreateNewPocket() {
    UUID pocketId = pocketService.createPocket("New pocket");

    PocketDto dto = transactionTemplate.execute(
        s -> em.find(Pocket.class, pocketId).toDto()
    );
    assertEquals("New pocket", dto.name());
  }

  @Test
  void shouldCreateTamagotchi() {
    UUID pocketId = pocketService.createPocket("New pocket");

    UUID tamagotchiId = pocketService.createTamagotchi(
        pocketId,
        new TamagotchiCreateRequest("my tamagotchi", CREATED)
    );

    PocketDto dto = transactionTemplate.execute(
        s -> em.find(Pocket.class, pocketId).toDto()
    );
    assertThat(dto.tamagotchis())
        .anyMatch(t ->
            t.name().equals("my tamagotchi")
                && t.status().equals(CREATED)
                && t.id().equals(tamagotchiId)
        );
  }

  @Test
  void shouldUpdateTamagotchi() {
    UUID pocketId = pocketService.createPocket("New pocket");
    UUID tamagotchiId = pocketService.createTamagotchi(
        pocketId,
        new TamagotchiCreateRequest("my tamagotchi", CREATED)
    );

    System.out.println("SQL----");
    pocketService.updateTamagotchi(
        tamagotchiId,
        new TamagotchiUpdateRequest("another tamagotchi", PENDING)
    );
    System.out.println("SQL----");

    PocketDto dto = transactionTemplate.execute(
        s -> em.find(Pocket.class, pocketId).toDto()
    );
    assertThat(dto.tamagotchis())
        .anyMatch(t ->
            t.name().equals("another tamagotchi")
                && t.status().equals(PENDING)
                && t.id().equals(tamagotchiId)
        );
  }

  @Test
  void shouldUpdateTamagotchiIfThereAreMultipleOnes() {
    UUID pocketId = pocketService.createPocket("New pocket");
    UUID tamagotchiId = pocketService.createTamagotchi(
        pocketId,
        new TamagotchiCreateRequest("Cat", CREATED)
    );
    pocketService.createTamagotchi(
        pocketId,
        new TamagotchiCreateRequest("Dog", CREATED)
    );

    System.out.println("SQL----");
    assertThrows(
        TamagotchiNameInvalidException.class,
        () -> pocketService.updateTamagotchi(tamagotchiId, new TamagotchiUpdateRequest("Dog", CREATED))
    );
  }
}