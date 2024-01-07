package br.com.fiap.techflix.application.usecases;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.infrastructure.controllers.dto.EstatisticaResponse;
import br.com.fiap.techflix.domain.Video;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

public class VideoUseCase {

    private final VideoGateway videoGateway;
    private final ResourceLoader resourceLoader;
    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");



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

    public Mono<Video> salvarVideo(String titulo, String categoria, Mono<FilePart> filePartMono) {
        Video video = new Video(titulo, categoria);
        return filePartMono
                .doOnNext(fp -> video.setNomeArquivo(fp.filename().replace(".mp4", "")))
                .flatMap(fp -> fp.transferTo(basePath.resolve(String.format(VIDEO_TYPE_FORMAT, video.getId()))))
                .then(videoGateway.salvarVideo(video));
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

    public Mono<Video> updateVideo(UUID videoId, String novoTitulo, String novaCategoria) {
        return videoGateway.updateVideo(videoId, novoTitulo, novaCategoria);
    }

    public Mono<Void> deleteVideo(UUID videoId) {
        return videoGateway.deleteVideo(videoId);
    }

    public Mono<Video> marcarDesmarcarFavorito(UUID videoId, boolean favorito) {
        return videoGateway.marcarDesmarcarFavorito(videoId, favorito);
    }

    public Flux<Video> buscarVideosRecomendadosPorFavoritos() {
        return videoGateway.buscarVideosRecomendadosPorFavoritos();
    }

    public Mono<EstatisticaResponse> obterEstatisticas() {
        return videoGateway.obterEstatisticas();
    }
}
