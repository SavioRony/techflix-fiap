package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.generate.GenerateObject;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import br.com.fiap.techflix.infrastructure.persistence.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VideoRepositoryGatewayTest {

    private AutoCloseable openMocks;

    private VideoRepositoryGateway gateway;

    @Mock
    private  VideoRepository repository;

    @Mock
    private  VideoEntityMapper mapper;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        gateway = new VideoRepositoryGateway(repository, mapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void buscarVideoUpdateVisualizacao() {

        var videoId = UUID.randomUUID();
        when(repository.findById(videoId)).thenReturn(GenerateObject.generateMonoVideoEntity());
        when(repository.save(any())).thenReturn(GenerateObject.generateMonoVideoEntity());
        when(mapper.toDomain(any())).thenReturn(GenerateObject.generateVideo());
        StepVerifier.create(gateway.buscarVideoUpdateVisualizacao(videoId)).verifyComplete();
    }

    @Test
    void salvarVideo() {

        var video = GenerateObject.generateVideo();
        when(mapper.toEntity(any())).thenReturn(GenerateObject.generateVideoEntity());
        when(repository.save(any(VideoEntity.class))).thenReturn(GenerateObject.generateMonoVideoEntity());

        var response = gateway.salvarVideo(video);
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPaginado() {

        var page = mock(PageRequest.class);
        when(repository.findAllByOrderByDataDeCadastroDesc(any(PageRequest.class))).thenReturn(GenerateObject.generateFluxVideoEntity());
        when(repository.count()).thenReturn(GenerateObject.generateMonoLong());
        when(mapper.mapEntitiesToDomain(any())).thenReturn(List.of(GenerateObject.generateVideo()));
        var response = gateway.buscarVideosPaginado(page);
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPorTitulo() {

        when(repository.findAllByTituloContainingIgnoreCaseOrderByDataDeCadastroDesc(any())).thenReturn(GenerateObject.generateFluxVideoEntity());
        var response = gateway.buscarVideosPorTitulo("titulo");
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPorPeriodo() {

        when(repository.findAllByDataDeCadastroBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(GenerateObject.generateFluxVideoEntity());
        var response = gateway.buscarVideosPorPeriodo(LocalDateTime.now(), LocalDateTime.now());
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPorCategoria() {
        when(repository.findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(any(String.class))).thenReturn(GenerateObject.generateFluxVideoEntity());
        var response = gateway.buscarVideosPorCategoria("categoria");
        assertThat(response).isNotNull();
    }

    @Test
    void updateVideo() {

        var videoId = UUID.randomUUID();
        when(repository.findById(videoId)).thenReturn(GenerateObject.generateMonoVideoEntity());
        when(repository.save(any())).thenReturn(GenerateObject.generateMonoVideoEntity());
        var response = gateway.updateVideo(videoId, "New Title", "New Catagory");
        assertThat(response).isNotNull();
    }

    @Test
    void deleteVideo() {

        when(repository.findById(any(UUID.class))).thenReturn(GenerateObject.generateMonoVideoEntity());
        var response = gateway.deleteVideo(UUID.randomUUID());
        assertThat(response).isNotNull();
    }

    @Test
    void marcarDesmarcarFavorito() {

        when(repository.findById(any(UUID.class))).thenReturn(GenerateObject.generateMonoVideoEntity());
        var response = gateway.marcarDesmarcarFavorito(UUID.randomUUID(), Boolean.TRUE);
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosRecomendadosPorFavoritos() {

        when(repository.findAllByFavoritoTrue()).thenReturn(GenerateObject.generateFluxVideoEntity());
        when(repository.findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(any())).thenReturn(GenerateObject.generateFluxVideoEntity());
        var response = gateway.buscarVideosRecomendadosPorFavoritos();
        assertThat(response).isNotNull();
    }

    @Test
    void obterEstatisticas() {

        when(repository.count()).thenReturn(GenerateObject.generateMonoLong());
        when(repository.countByFavoritoTrue()).thenReturn(GenerateObject.generateMonoLong());
        when(repository.averageVisualizacao()).thenReturn(GenerateObject.generateMonoDouble());
        var response = gateway.obterEstatisticas();
        assertThat(response).isNotNull();
    }
}