package br.com.fiap.techflix.infrastructure.controllers;

import br.com.fiap.techflix.application.usecases.VideoUseCase;
import br.com.fiap.techflix.generate.GenerateObject;
import br.com.fiap.techflix.infrastructure.controllers.dto.EstatisticaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class VideoControllerTest {



    @Mock
    private VideoUseCase useCase;

    @InjectMocks
    private VideoController controller;

    @BeforeEach
    void setUp(){
        BDDMockito.when(useCase.buscarVideosPorTitulo(any())).thenReturn(Flux.just(GenerateObject.generateVideo()));
        BDDMockito.when(useCase.salvarVideo(any(), any(), any())).thenReturn(GenerateObject.generateMonoVideo());
    }

    @Test
    void getVideo(){
        StepVerifier.create(controller.getVideo(UUID.randomUUID().toString(), "000000-")).expectComplete();
    }

    @Test
    void uploadVideo() {

        var filePart = Mockito.mock(FilePart.class);
        StepVerifier.create(controller.uploadVideo("title", "category",Mono.just(filePart))).expectComplete();
    }

    @Test
    void getVideos() {

        StepVerifier.create(controller.getVideos(0,10)).expectComplete();
    }

    @Test
    void buscarVideoPorTitulo() throws Exception {
        StepVerifier.create(controller.buscarVideoPorTitulo("Titulo")).expectComplete();
    }

    @Test
    void buscarVideosPorPeriodo() {

        BDDMockito.when(useCase.buscarVideosPorPeriodo(any(),any())).thenReturn(GenerateObject.generateFluxVideo());
        StepVerifier.create(controller.buscarVideosPorPeriodo(LocalDateTime.now(), LocalDateTime.now())).expectComplete();
    }

    @Test
    void buscarVideoPorCategoria() {
        BDDMockito.when(useCase.buscarVideosPorCategoria(any())).thenReturn(GenerateObject.generateFluxVideo());
        StepVerifier.create(controller.buscarVideoPorCategoria("category")).expectComplete();
    }

    @Test
    void updateVideo() {
        BDDMockito.when(useCase.updateVideo(any(),any(),any())).thenReturn(GenerateObject.generateMonoVideo());
        StepVerifier.create(controller.updateVideo(UUID.randomUUID(),"new Title", "new category")).expectComplete();
    }

    @Test
    void deleteVideo() {
        StepVerifier.create(controller.deleteVideo(UUID.randomUUID())).expectComplete();
    }

    @Test
    void marcarDesmarcarFavorito() {
        BDDMockito.when(useCase.marcarDesmarcarFavorito(any(),any(Boolean.class))).thenReturn(GenerateObject.generateMonoVideo());
        StepVerifier.create(controller.marcarDesmarcarFavorito(UUID.randomUUID(),Boolean.TRUE)).expectComplete();

    }

    @Test
    void testBuscarVideoPorCategoria() {
        BDDMockito.when(useCase.buscarVideosRecomendadosPorFavoritos()).thenReturn(GenerateObject.generateFluxVideo());
        StepVerifier.create(controller.buscarVideoPorCategoria()).expectComplete();
    }

    @Test
    void obterEstatisticas() {
        BDDMockito.when(useCase.obterEstatisticas()).thenReturn(Mono.just(new EstatisticaResponse(1,2,3.3)));
        StepVerifier.create(controller.obterEstatisticas()).expectComplete();
    }
}