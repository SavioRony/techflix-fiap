package br.com.fiap.techflix.infrastructure.gateways;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.infrastructure.controllers.dto.EstatisticaResponse;
import br.com.fiap.techflix.domain.Video;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import br.com.fiap.techflix.infrastructure.persistence.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class VideoRepositoryGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    private final VideoEntityMapper videoEntityMapper;

    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");

    private final Random random = new Random();


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

    @Override
    public Mono<Video> updateVideo(UUID videoId, String novoTitulo, String novaCategoria) {
        return videoRepository.findById(videoId)
                .flatMap(video -> {
                    video.setTitulo(novoTitulo);
                    video.setCategoria(novaCategoria);
                    return videoRepository.save(video);
                }).map(videoEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteVideo(UUID videoId) {
        return videoRepository.findById(videoId)
                .flatMap(video -> {
                    Path videoFilePath = basePath.resolve(String.format(VIDEO_TYPE_FORMAT, video.getId()));
                    try {
                        Files.delete(videoFilePath);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                    return videoRepository.delete(video);
                })
                .then();
    }

    @Override
    public Mono<Video> marcarDesmarcarFavorito(UUID videoId, boolean favorito) {
        return videoRepository.findById(videoId)
                .flatMap(video -> {
                    video.setFavorito(favorito);
                    return videoRepository.save(video);
                }).map(videoEntityMapper::toDomain);
    }

    @Override
    public Flux<Video> buscarVideosRecomendadosPorFavoritos() {
        Flux<VideoEntity> favoritos = videoRepository.findAllByFavoritoTrue();

        return favoritos
                .collectList()
                .flatMapMany(favoritosList -> {
                    if (favoritosList.isEmpty()) {
                        return Flux.empty();
                    }

                    VideoEntity videoEntityAleatorio = favoritosList.get(random.nextInt(favoritosList.size()));
                    String categoriaVideoAleatorio = videoEntityAleatorio.getCategoria();

                    return videoRepository.findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(
                                    categoriaVideoAleatorio)
                            .take(10);
                }).map(videoEntityMapper::toDomain);
    }

    @Override
    public Mono<EstatisticaResponse> obterEstatisticas() {
        Mono<Long> totalVideos = videoRepository.count();
        Mono<Long> videosFavoritados = videoRepository.countByFavoritoTrue();
        Mono<Double> mediaVisualizacoes = videoRepository.averageVisualizacao();

        return Mono.zip(totalVideos, videosFavoritados, mediaVisualizacoes)
                .map(tuple -> new EstatisticaResponse(tuple.getT1(), tuple.getT2(), tuple.getT3() * 100));
    }
}
