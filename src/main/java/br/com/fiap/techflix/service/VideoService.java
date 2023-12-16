package br.com.fiap.techflix.service;

import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import br.com.fiap.techflix.domain.dto.Estatisticas;
import br.com.fiap.techflix.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private ResourceLoader resourceLoader;
    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");

    @Autowired
    private VideoRepository videoRepository;

//    public Mono<Resource> getVideo(String id, String range) {
//        boolean visualizacao = range.substring(6).equals("0-");
//        System.out.println("Visualização: " + visualizacao);
//        String filePath = String.format("file:./src/main/resources/uploads/%s.mp4", id);
//        Resource resource = this.resourceLoader.getResource(filePath);
//        if (visualizacao) {
//            return videoRepository.findById(UUID.fromString(id)).flatMap(video -> {
//                video.setVisualizacao(video.getVisualizacao() + 1);
//                return videoRepository.save(video);
//            }).then(Mono.fromSupplier(() -> resource));
//        }
//
//        return Mono.fromSupplier(() -> resource);
//    }
//
//    public Mono<VideoEntity> uploadVideo(String titulo, String categoria, Mono<FilePart> filePartMono) {
//        VideoEntity videoEntity = new VideoEntity(titulo, categoria);
//        return filePartMono
//                .doOnNext(fp -> videoEntity.setNomeArquivo(fp.filename().replace(".mp4", "")))
//                .flatMap(fp -> fp.transferTo(basePath.resolve(String.format(VIDEO_TYPE_FORMAT, videoEntity.getId()))))
//                .then(Mono.defer(() -> videoRepository.save(videoEntity)));
//    }

    public Mono<Page<VideoEntity>> buscarVideosPaginado(Pageable pageable) {
        return videoRepository.findAllByOrderByDataDeCadastroDesc(pageable).collectList()
                .zipWith(this.videoRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    public Flux<VideoEntity> buscarVideosPorTitulo(String titulo) {
        return videoRepository.findAllByTituloContainingIgnoreCaseOrderByDataDeCadastroDesc(titulo);
    }

    public Flux<VideoEntity> buscarVideosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return videoRepository.findAllByDataDeCadastroBetween(dataInicio, dataFim);
    }

    public Flux<VideoEntity> buscarVideosPorCategoria(String categoria) {
        return videoRepository.findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(categoria);
    }

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

                    VideoEntity videoEntityAleatorio = favoritosList.get(new Random().nextInt(favoritosList.size()));
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

