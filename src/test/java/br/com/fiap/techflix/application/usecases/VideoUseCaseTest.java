package br.com.fiap.techflix.application.usecases;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.generate.GenerateObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoUseCaseTest {

    private AutoCloseable openMocks;

    private VideoUseCase videoUseCase;

    @Mock
    private  VideoGateway videoGateway;

    @Mock
    private  ResourceLoader resourceLoader;

    @BeforeEach
    void setUp(){

        openMocks = MockitoAnnotations.openMocks(this);
        videoUseCase = new VideoUseCase(videoGateway, resourceLoader);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void buscarVideo() {

        var resource = mock(Resource.class);
        var uuid = UUID.randomUUID();
        when(resourceLoader.getResource(any(String.class))).thenReturn(resource);
        Mockito.when(videoGateway.buscarVideoUpdateVisualizacao(any(UUID.class))).thenReturn(GenerateObject.generateMonoVideo());
        var response = videoUseCase.buscarVideo(uuid.toString(), "0000000-");
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideo_quandoVisualizaçãoFalsa() {

        var resource = mock(Resource.class);
        when(videoGateway.buscarVideoUpdateVisualizacao(any(UUID.class))).thenReturn(GenerateObject.generateMonoVideo());
        when(resourceLoader.getResource(any(String.class))).thenReturn(resource);
        var response = videoUseCase.buscarVideo(UUID.randomUUID().toString(), "0000002-");
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPaginado() {

        var mono = Mockito.mock(Mono.class);
        when(videoGateway.buscarVideosPaginado(any(PageRequest.class))).thenReturn(mono);

        var response = videoUseCase.buscarVideosPaginado(0,3);
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPorTitulo() {

        when(videoGateway.buscarVideosPorTitulo(any(String.class))).thenReturn(GenerateObject.generateFluxVideo());
        var response = videoUseCase.buscarVideosPorTitulo("Teste");
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPorPeriodo() {

        when(videoGateway.buscarVideosPorPeriodo(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(GenerateObject.generateFluxVideo());
        var response = videoUseCase.buscarVideosPorPeriodo(LocalDateTime.now(), LocalDateTime.now());
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosPorCategoria() {

        when(videoGateway.buscarVideosPorCategoria(any(String.class))).thenReturn(GenerateObject.generateFluxVideo());
        var response = videoUseCase.buscarVideosPorCategoria("terror");
        assertThat(response).isNotNull();
    }

    @Test
    void updateVideo() {

        when(videoGateway.updateVideo(any(UUID.class),any(String.class),any(String.class))).thenReturn(GenerateObject.generateMonoVideo());
        var response = videoUseCase.updateVideo(UUID.randomUUID(), "A volta dos que não foram", "terror");
        assertThat(response).isNotNull();
    }

    @Test
    void deleteVideo() {

        var mono = Mockito.mock(Mono.class);
        when(videoGateway.deleteVideo(any(UUID.class))).thenReturn(mono);
        var response = videoUseCase.deleteVideo(UUID.randomUUID());
        assertThat(response).isNotNull();
    }

    @Test
    void marcarDesmarcarFavorito() {

        var mono = Mockito.mock(Mono.class);
        when(videoGateway.marcarDesmarcarFavorito(any(UUID.class),any(boolean.class))).thenReturn(GenerateObject.generateMonoVideo());
        var response = videoUseCase.marcarDesmarcarFavorito(UUID.randomUUID(), Boolean.TRUE);
        assertThat(response).isNotNull();
    }

    @Test
    void buscarVideosRecomendadosPorFavoritos() {

        when(videoGateway.buscarVideosRecomendadosPorFavoritos()).thenReturn(GenerateObject.generateFluxVideo());
        var response = videoUseCase.buscarVideosRecomendadosPorFavoritos();
        assertThat(response).isNotNull();
    }

    @Test
    void obterEstatisticas() {

        var mono = Mockito.mock(Mono.class);
        when(videoGateway.obterEstatisticas()).thenReturn(mono);
        var response = videoUseCase.obterEstatisticas();
        assertThat(response).isNotNull();
    }
}