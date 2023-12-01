package br.com.fiap.techflix.repository;

import br.com.fiap.techflix.model.Video;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface VideoRepository extends ReactiveMongoRepository<Video, UUID> {
}
