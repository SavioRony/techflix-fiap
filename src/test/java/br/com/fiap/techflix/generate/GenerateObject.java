package br.com.fiap.techflix.generate;

import br.com.fiap.techflix.domain.Video;
import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public class GenerateObject {

    public static Video generateVideo(){
        return new Video(
                UUID.randomUUID(),
                "TESTE",
                "Teste Class",
                LocalDateTime.now(),
                "src/main/resources",
                "Terror",
                0,
                false
        );
    }

    public static VideoEntity generateVideoEntity(){
        return new VideoEntity(
                UUID.randomUUID(),
                "TESTE",
                "Teste Class",
                LocalDateTime.now(),
                "src/main/resources",
                "Terror",
                0,
                false
        );
    }

    public static Mono<VideoEntity> generateMonoVideoEntity(){

        return Mono.zip(variable -> new VideoEntity(
                UUID.randomUUID(),
                "TESTE",
                "Teste Class",
                LocalDateTime.now(),
                "src/main/resources",
                "Terror",
                0,
                false
        ));
    }

    public static Mono<Long> generateMonoLong(){

        return Mono.zip(variable -> 4L);
    }

    public static Mono<Double> generateMonoDouble(){

        return Mono.zip(variable -> 9.45);
    }

    public static Flux<VideoEntity> generateFluxVideoEntity(){

        return Flux.zip(variable -> new VideoEntity(
                UUID.randomUUID(),
                "TESTE",
                "Teste Class",
                LocalDateTime.now(),
                "src/main/resources",
                "Terror",
                0,
                false
        ));
    }

    public static Mono<Video> generateMonoVideo(){

        return Mono.zip(variable -> generateVideo());
    }

    public static Flux<Video> generateFluxVideo(){

        return Flux.zip(variable -> generateVideo());
    }
}
