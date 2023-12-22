package br.com.fiap.techflix.infrastructure.controllers;

import br.com.fiap.techflix.application.usecases.VideoUseCase;
import br.com.fiap.techflix.generate.GenerateObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VideoControllerTest {

    private MockMvc mockMvc;

    private AutoCloseable openMock;

    @Mock
    private VideoUseCase useCase;

    @BeforeEach
    void setUp() {
        openMock = MockitoAnnotations.openMocks(this);
        var videoController = new VideoController(useCase);
        mockMvc = MockMvcBuilders.standaloneSetup(videoController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMock.close();
    }

    @Test
    void getVideo() throws Exception {

        mockMvc.perform(get("/videos")
                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void uploadVideo() {
    }

    @Test
    void getVideos() {
    }

    @Test
    void buscarVideoPorTitulo() throws Exception {

        when(useCase.buscarVideosPorTitulo(any())).thenReturn(GenerateObject.generateFluxVideo());

        mockMvc.perform(get("/videos/titulos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void buscarVideosPorPeriodo() {
    }

    @Test
    void buscarVideoPorCategoria() {
    }

    @Test
    void updateVideo() {
    }

    @Test
    void deleteVideo() {
    }

    @Test
    void marcarDesmarcarFavorito() {
    }

    @Test
    void testBuscarVideoPorCategoria() {
    }

    @Test
    void obterEstatisticas() {
    }
}