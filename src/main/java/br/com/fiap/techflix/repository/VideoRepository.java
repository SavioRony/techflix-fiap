package br.com.fiap.techflix.repository;

import br.com.fiap.techflix.infrastructure.persistence.VideoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface VideoRepository extends ReactiveMongoRepository<VideoEntity, UUID> {

    Flux<VideoEntity> findAllByOrderByDataDeCadastroDesc(Pageable pageable);
    Flux<VideoEntity> findAllByTituloContainingIgnoreCaseOrderByDataDeCadastroDesc(String titulo);

    @Query("{ 'dataDeCadastro' : { $gte: ?0, $lte: ?1 } }")
    Flux<VideoEntity> findAllByDataDeCadastroBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    Flux<VideoEntity> findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(String categoria);

    Flux<VideoEntity> findAllByFavoritoTrue();

    Mono<Long> countByFavoritoTrue();

    @Aggregation("{ $group: { _id: null, mediaVisualizacoes: { $avg: '$visualizacao' } } }")
    Mono<Double> averageVisualizacao();

}
