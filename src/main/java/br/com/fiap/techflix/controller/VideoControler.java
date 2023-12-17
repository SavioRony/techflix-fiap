package br.com.fiap.techflix.controller;

import br.com.fiap.techflix.domain.dto.Estatisticas;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import br.com.fiap.techflix.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RestController
@RequestMapping("videos")
public class VideoControler {

    @Autowired
    private VideoService videoService;


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
    public Flux<VideoEntity> buscarVideoPorCategoria() {
        return videoService.buscarVideosRecomendadosPorFavoritos();
    }

    @GetMapping("/estatisticas")
    public Mono<Estatisticas> obterEstatisticas() {
        return videoService.obterEstatisticas();
    }

}
