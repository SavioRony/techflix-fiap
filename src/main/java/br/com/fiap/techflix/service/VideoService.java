package br.com.fiap.techflix.service;

import br.com.fiap.techflix.domain.dto.Estatisticas;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import br.com.fiap.techflix.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

@Service
public class VideoService {

    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");

    @Autowired
    private VideoRepository videoRepository;

    private final Random random = new Random();

    public Mono<VideoEntity> updateVideo(UUID videoId, String novoTitulo, String novaCategoria) {
        return videoRepository.findById(videoId)
                .flatMap(video -> {
                    video.setTitulo(novoTitulo);
                    video.setCategoria(novaCategoria);
                    return videoRepository.save(video);
                });
    }

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

    public Mono<VideoEntity> marcarDesmarcarFavorito(UUID videoId, boolean favorito) {
        return videoRepository.findById(videoId)
                .flatMap(video -> {
                    video.setFavorito(favorito);
                    return videoRepository.save(video);
                });
    }

    public Flux<VideoEntity> buscarVideosRecomendadosPorFavoritos() {
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
                });
    }

    public Mono<Estatisticas> obterEstatisticas() {
        Mono<Long> totalVideos = videoRepository.count();
        Mono<Long> videosFavoritados = videoRepository.countByFavoritoTrue();
        Mono<Double> mediaVisualizacoes = videoRepository.averageVisualizacao();

        return Mono.zip(totalVideos, videosFavoritados, mediaVisualizacoes)
                .map(tuple -> new Estatisticas(tuple.getT1(), tuple.getT2(), tuple.getT3() * 100));
    }

}

