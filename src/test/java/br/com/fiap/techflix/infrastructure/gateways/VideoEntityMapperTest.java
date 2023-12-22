package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VideoEntityMapperTest {

    private AutoCloseable openMock;

    private VideoEntityMapper mapper;

    @BeforeEach
    void setUp(){
        openMock = MockitoAnnotations.openMocks(this);
        mapper = new VideoEntityMapper();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMock.close();
    }

    @Test
    void testToDomainAndToEntity() {

        var entity = new VideoEntity(
                UUID.randomUUID(),
                "TESTE",
                "Teste Class",
                LocalDateTime.now(),
                "src/main/resources",
                "Terror",
                0,
                false
        );

        var response = mapper.toEntity(mapper.toDomain(entity));
        assertThat(response).isNotNull();
    }

    @Test
    void mapEntitiesToDomain() {

        var entity = new VideoEntity(
                UUID.randomUUID(),
                "TESTE",
                "Teste Class",
                LocalDateTime.now(),
                "src/main/resources",
                "Terror",
                0,
                false
        );

        var response = mapper.mapEntitiesToDomain(List.of(entity, entity, entity));
        assertThat(response.size()).isEqualTo(3);
    }
}