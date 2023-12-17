package br.com.fiap.techflix.application.usecases;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.domain.entity.Video;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public class VideoUseCase {

    private final VideoGateway videoGateway;
    private final ResourceLoader resourceLoader;


    public VideoUseCase(VideoGateway videoGateway, ResourceLoader resourceLoader) {
        this.videoGateway = videoGateway;
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> buscarVideo(String id, String range) {
        boolean visualizacao = range.substring(6).equals("0-");
        String filePath = String.format("file:./src/main/resources/uploads/%s.mp4", id);
        Resource resource = this.resourceLoader.getResource(filePath);
        if (visualizacao) {
            return videoGateway.buscarVideoUpdateVisualizacao(UUID.fromString(id)).then(Mono.fromSupplier(() -> resource));
        }
        return Mono.fromSupplier(() -> resource);
    }

    public Mono<Video> salvarVideo(Video video) {
        return videoGateway.salvarVideo(video);
    }

    public Mono<Page<Video>> buscarVideosPaginado(int page, int size) {
        return videoGateway.buscarVideosPaginado(PageRequest.of(page, size));
    }

    public Flux<Video> buscarVideosPorTitulo(String titulo) {
        return videoGateway.buscarVideosPorTitulo(titulo);
    }

    public Flux<Video> buscarVideosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return videoGateway.buscarVideosPorPeriodo(dataInicio, dataFim);
    }

    public Flux<Video> buscarVideosPorCategoria(String categoria) {
        return videoGateway.buscarVideosPorCategoria(categoria);
    }
}
