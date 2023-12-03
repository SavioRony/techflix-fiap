package br.com.fiap.techflix.service;

import br.com.fiap.techflix.model.Video;
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
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private ResourceLoader resourceLoader;
    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");

    @Autowired
    private VideoRepository videoRepository;

    public Mono<Resource> getVideo(String title) {
        String filePath = String.format("file:./src/main/resources/uploads/%s.mp4", title);
        Resource resource = this.resourceLoader.getResource(filePath);
        return Mono.fromSupplier(() -> resource);
    }

    public Mono<Video> uploadVideo(String titulo, String categoria, Mono<FilePart> filePartMono) {
        Video video = new Video(titulo, categoria);
        return filePartMono
                .doOnNext(fp -> video.setNomeArquivo(fp.filename().replace(".mp4", "")))
                .flatMap(fp -> fp.transferTo(basePath.resolve(String.format(VIDEO_TYPE_FORMAT, video.getId()))))
                .then(Mono.defer(() -> videoRepository.save(video)));
    }

    public Mono<Page<Video>> buscarVideosPaginado(Pageable pageable) {
        return videoRepository.findAllByOrderByDataDeCadastroDesc(pageable).collectList()
                .zipWith(this.videoRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    public Flux<Video> buscarVideosPorTitulo(String titulo) {
        return videoRepository.findAllByTituloContainingIgnoreCaseOrderByDataDeCadastroDesc(titulo);
    }

    public Flux<Video> buscarVideosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return videoRepository.findAllByDataDeCadastroBetween(dataInicio, dataFim);
    }

    public Flux<Video> buscarVideosPorCategoria(String categoria) {
        return videoRepository.findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(categoria);
    }

    public Mono<Video> updateVideo(UUID videoId, String novoTitulo, String novaCategoria) {
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
}

