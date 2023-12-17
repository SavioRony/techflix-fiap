package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.domain.Video;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoEntityMapper {

    Video toDomain(VideoEntity videoEntity){
        return new Video(
                videoEntity.getId(),
                videoEntity.getTitulo(),
                videoEntity.getNomeArquivo(),
                videoEntity.getDataDeCadastro(),
                videoEntity.getPath(),
                videoEntity.getCategoria(),
                videoEntity.getVisualizacao(),
                videoEntity.getFavorito());
    }

    VideoEntity toEntity(Video video){
        return new VideoEntity(
                video.getId(),
                video.getTitulo(),
                video.getNomeArquivo(),
                video.getDataDeCadastro(),
                video.getPath(),
                video.getCategoria(),
                video.getVisualizacao(),
                video.getFavorito());
    }

    public List<Video> mapEntitiesToDomain(List<VideoEntity> videoEntities) {
        return videoEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

}
