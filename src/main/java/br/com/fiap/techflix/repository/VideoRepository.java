package br.com.fiap.techflix.repository;

import br.com.fiap.techflix.model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface VideoRepository extends ReactiveMongoRepository<Video, UUID> {
    Flux<Video> findAllBy(Pageable pageable);
}
