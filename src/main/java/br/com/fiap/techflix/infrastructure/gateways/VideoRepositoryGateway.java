package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.domain.entity.Video;
import br.com.fiap.techflix.repository.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
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

    @Override
    public Mono<Page<Video>> buscarVideosPaginado(PageRequest pageable) {
        return videoRepository.findAllByOrderByDataDeCadastroDesc(pageable).collectList()
                .zipWith(this.videoRepository.count())
                .map(p -> new PageImpl<>(videoEntityMapper.mapEntitiesToDomain(p.getT1()), pageable, p.getT2()));
    }

    @Override
    public Flux<Video> buscarVideosPorTitulo(String titulo) {
        return videoRepository.findAllByTituloContainingIgnoreCaseOrderByDataDeCadastroDesc(titulo)
                .map(videoEntityMapper::toDomain);
    }

    @Override
    public Flux<Video> buscarVideosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return videoRepository.findAllByDataDeCadastroBetween(dataInicio, dataFim)
                .map(videoEntityMapper::toDomain);
    }

    @Override
    public Flux<Video> buscarVideosPorCategoria(String categoria) {
        return videoRepository.findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(categoria)
                .map(videoEntityMapper::toDomain);
    }
}
