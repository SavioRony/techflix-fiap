package br.com.fiap.techflix.application.gateway;

import br.com.fiap.techflix.infrastructure.controllers.dto.EstatisticaResponse;
import br.com.fiap.techflix.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface VideoGateway {

    Mono<Video> buscarVideoUpdateVisualizacao(UUID id);

    Mono<Video> salvarVideo(Video video);

    Mono<Page<Video>> buscarVideosPaginado(PageRequest of);

    Flux<Video> buscarVideosPorTitulo(String titulo);

    Flux<Video> buscarVideosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);

    Flux<Video> buscarVideosPorCategoria(String categoria);

    Mono<Video> updateVideo(UUID videoId, String novoTitulo, String novaCategoria);

    Mono<Void> deleteVideo(UUID videoId);

    Mono<Video> marcarDesmarcarFavorito(UUID videoId, boolean favorito);

    Flux<Video> buscarVideosRecomendadosPorFavoritos();

    Mono<EstatisticaResponse> obterEstatisticas();
}
