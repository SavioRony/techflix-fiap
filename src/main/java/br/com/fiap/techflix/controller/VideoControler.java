package br.com.fiap.techflix.controller;

import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import br.com.fiap.techflix.domain.dto.Estatisticas;
import br.com.fiap.techflix.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("videos")
public class VideoControler {

    @Autowired
    private VideoService videoService;

//    @PostMapping("/upload")
//    public Mono<Video> uploadVideo(@RequestPart("titulo") String titulo,
//                                   @RequestPart("categoria") String categoria,
//                                   @RequestPart("fileToUpload") Mono<FilePart> filePartMono) {
//        return videoService.uploadVideo(titulo,categoria, filePartMono);
//    }
//
//    @GetMapping(value = "{id}", produces = "video/mp4")
//    public Mono<Resource> getVideo(@PathVariable("id") String id, @RequestHeader("range") String range) {
//        return videoService.getVideo(id, range);
//    }

    @GetMapping
    public Mono<Page<VideoEntity>> getVideos(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size){
        return videoService.buscarVideosPaginado(PageRequest.of(page, size));
    }

    @GetMapping("/titulo")
    public Flux<VideoEntity> buscarVideoPorTitulo(@RequestParam("titulo") String titulo){
        return videoService.buscarVideosPorTitulo(titulo);
    }

    @GetMapping("/data")
    public Flux<VideoEntity> buscarVideosPorPeriodo(@RequestParam("dataInicio") LocalDateTime dataInicio,
                                                    @RequestParam("dataFim") LocalDateTime dataFim){
        return videoService.buscarVideosPorPeriodo(dataInicio, dataFim);
    }

    @GetMapping("/categoria")
    public Flux<VideoEntity> buscarVideoPorCategoria(@RequestParam("categoria") String categoria){
        return videoService.buscarVideosPorCategoria(categoria);
    }

    @PutMapping("/{videoId}/update")
    public Mono<VideoEntity> updateVideo(@PathVariable UUID videoId,
                                         @RequestPart("novoTitulo") String novoTitulo,
                                         @RequestPart("novaCategoria") String novaCategoria) {
        return videoService.updateVideo(videoId, novoTitulo, novaCategoria);
    }

    @DeleteMapping("/{videoId}/delete")
    public Mono<Void> deleteVideo(@PathVariable UUID videoId) {
        return videoService.deleteVideo(videoId);
    }

    @PutMapping("/{videoId}/favorito")
    public Mono<VideoEntity> marcarDesmarcarFavorito(@PathVariable UUID videoId, @RequestParam boolean favorito) {
        return videoService.marcarDesmarcarFavorito(videoId, favorito);
    }

    @GetMapping("/recomendados")
    public Flux<VideoEntity> buscarVideoPorCategoria(){
        return videoService.buscarVideosRecomendadosPorFavoritos();
    }
    @GetMapping("/estatisticas")
    public Mono<Estatisticas> obterEstatisticas() {
        return videoService.obterEstatisticas();
    }

}
