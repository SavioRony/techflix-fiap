package br.com.fiap.techflix.application.usecases;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.domain.entity.Video;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class VideoUseCase {

    private final VideoGateway videoGateway;
    private final ResourceLoader resourceLoader;


    public VideoUseCase(VideoGateway videoGateway, ResourceLoader resourceLoader) {
        this.videoGateway = videoGateway;
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> getVideo(String id, String range) {
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

}
