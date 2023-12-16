package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.domain.entity.Video;
import br.com.fiap.techflix.repository.VideoRepository;
import reactor.core.publisher.Mono;


import java.util.UUID;

public class VideoRepositoryGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    private final VideoEntityMapper videoEntityMapper;


    public VideoRepositoryGateway(VideoRepository videoRepository, VideoEntityMapper videoEntityMapper) {
        this.videoRepository = videoRepository;
        this.videoEntityMapper = videoEntityMapper;
    }

    @Override
    public Mono<Video> buscarVideoUpdateVisualizacao(UUID id) {
       return videoRepository.findById(id).flatMap(video -> {
            video.setVisualizacao(video.getVisualizacao() + 1);
            return videoRepository.save(video);
        }).map(videoEntityMapper::toDomain);
    }

    @Override
    public Mono<Video> salvarVideo(Video video) {
        return videoRepository.save(videoEntityMapper.toEntity(video)).map(videoEntityMapper::toDomain);
    }
}
