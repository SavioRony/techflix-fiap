package br.com.fiap.techflix.infrastructure.controllers;

import br.com.fiap.techflix.application.usecases.VideoUseCase;
import br.com.fiap.techflix.infrastructure.controllers.dto.EstatisticaResponse;
import br.com.fiap.techflix.domain.Video;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("videos")
public class VideoController {

    private final VideoUseCase videoUseCase;
    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");


    public VideoController(VideoUseCase videoUseCase) {
        this.videoUseCase = videoUseCase;
    }

    @GetMapping(value = "{id}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable("id") String id, @RequestHeader("range") String range) {
        return videoUseCase.buscarVideo(id, range);
    }

    @PostMapping("/upload")
    public Mono<Video> uploadVideo(@RequestPart("titulo") String titulo,
                                   @RequestPart("categoria") String categoria,
                                   @RequestPart("fileToUpload") Mono<FilePart> filePartMono) {
        Video video = new Video(titulo, categoria);
        return filePartMono
                .doOnNext(fp -> video.setNomeArquivo(fp.filename().replace(".mp4", "")))
                .flatMap(fp -> fp.transferTo(basePath.resolve(String.format(VIDEO_TYPE_FORMAT, video.getId()))))
                .then(videoUseCase.salvarVideo(video));
    }

    @GetMapping
    public Mono<Page<Video>> getVideos(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return videoUseCase.buscarVideosPaginado(page, size);
    }

    @GetMapping("/titulo")
    public Flux<Video> buscarVideoPorTitulo(@RequestParam("titulo") String titulo) {
        return videoUseCase.buscarVideosPorTitulo(titulo);
    }

    @GetMapping("/data")
    public Flux<Video> buscarVideosPorPeriodo(@RequestParam("dataInicio") LocalDateTime dataInicio,
                                              @RequestParam("dataFim") LocalDateTime dataFim) {
        return videoUseCase.buscarVideosPorPeriodo(dataInicio, dataFim);
    }

    @GetMapping("/categoria")
    public Flux<Video> buscarVideoPorCategoria(@RequestParam("categoria") String categoria) {
        return videoUseCase.buscarVideosPorCategoria(categoria);
    }

    @PutMapping("/{videoId}/update")
    public Mono<Video> updateVideo(@PathVariable UUID videoId,
                                         @RequestPart("novoTitulo") String novoTitulo,
                                         @RequestPart("novaCategoria") String novaCategoria) {
        return videoUseCase.updateVideo(videoId, novoTitulo, novaCategoria);
    }

    @DeleteMapping("/{videoId}/delete")
    public Mono<Void> deleteVideo(@PathVariable UUID videoId) {
        return videoUseCase.deleteVideo(videoId);
    }

    @PutMapping("/{videoId}/favorito")
    public Mono<Video> marcarDesmarcarFavorito(@PathVariable UUID videoId, @RequestParam boolean favorito) {
        return videoUseCase.marcarDesmarcarFavorito(videoId, favorito);
    }

    @GetMapping("/recomendados")
    public Flux<Video> buscarVideoPorCategoria() {
        return videoUseCase.buscarVideosRecomendadosPorFavoritos();
    }

    @GetMapping("/estatisticas")
    public Mono<EstatisticaResponse> obterEstatisticas() {
        return videoUseCase.obterEstatisticas();
    }

}
