package br.com.fiap.techflix.application.gateway;

import br.com.fiap.techflix.domain.entity.Video;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VideoGateway {

    Mono<Video> buscarVideoUpdateVisualizacao(UUID id);

    Mono<Video> salvarVideo(Video video);
}
