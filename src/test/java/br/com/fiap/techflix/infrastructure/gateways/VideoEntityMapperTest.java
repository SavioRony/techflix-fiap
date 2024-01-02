package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.domain.Video;
import br.com.fiap.techflix.generate.GenerateObject;
import br.com.fiap.techflix.infrastructure.controllers.dto.EstatisticaResponse;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Test
    void testConstructorVideo(){
        var video = new Video("Title", "Category");
        assertThat(video).isNotNull();
        assertThat(video.getId()).isNotNull();
    }

    @Test
    void testVideoEntity(){
        var video = GenerateObject.generateVideoEntity();

        var testVideoEntity = new VideoEntity();
        testVideoEntity.setId(video.getId());
        testVideoEntity.setFavorito(video.getFavorito());
        testVideoEntity.setCategoria(video.getCategoria());
        testVideoEntity.setPath(video.getPath());
        testVideoEntity.setTitulo(video.getTitulo());
        testVideoEntity.setVisualizacao(video.getVisualizacao());
        testVideoEntity.setDataDeCadastro(video.getDataDeCadastro());
        assertThat(testVideoEntity.getId()).isEqualTo(video.getId());
        assertThat(testVideoEntity.toString()).isNotNull();
    }

    @Test
    void testVideo(){
        var video = GenerateObject.generateVideo();

        var videoDto = new Video();
        videoDto.setId(video.getId());
        videoDto.setFavorito(video.getFavorito());
        videoDto.setCategoria(video.getCategoria());
        videoDto.setPath(video.getPath());
        videoDto.setTitulo(video.getTitulo());
        videoDto.setVisualizacao(video.getVisualizacao());
        videoDto.setDataDeCadastro(video.getDataDeCadastro());
        assertThat(videoDto.getId()).isEqualTo(video.getId());
    }

    @Test
    void testDtoEstatisticaResponse(){

        var dto = new EstatisticaResponse(60, 12, 24.5);
        assertThat(dto.getMediaVisualizacoes()).isEqualTo(24.5);
        assertThat(dto.getVideosFavoritados()).isEqualTo(12);
        assertThat(dto.getTotalVideos()).isEqualTo(60);
    }

    @Test
    void testEstatisticaResponse(){
        var dto = new EstatisticaResponse();
        dto.setTotalVideos(23);
        dto.setVideosFavoritados(4);
        dto.setMediaVisualizacoes(34.78);

        assertThat(dto.getMediaVisualizacoes()).isEqualTo(34.78);
        assertThat(dto.getTotalVideos()).isEqualTo(23);
        assertThat(dto.getVideosFavoritados()).isEqualTo(4);
    }
}