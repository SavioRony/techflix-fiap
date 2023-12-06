package br.com.fiap.techflix.repository;

import br.com.fiap.techflix.model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends ReactiveMongoRepository<Video, UUID> {

    Flux<Video> findAllByOrderByDataDeCadastroDesc(Pageable pageable);
    Flux<Video> findAllByTituloContainingIgnoreCaseOrderByDataDeCadastroDesc(String titulo);

    @Query("{ 'dataDeCadastro' : { $gte: ?0, $lte: ?1 } }")
    Flux<Video> findAllByDataDeCadastroBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    Flux<Video> findAllByCategoriaContainingIgnoreCaseOrderByDataDeCadastroDesc(String categoria);

    Flux<Video> findAllByFavoritoTrue();

    Flux<Video> findAllByCategoriaContainingIgnoreCaseAndIdNotInOrderByDataDeCadastroDesc(String categoriaVideoAleatorio, List<UUID> collect);

    Mono<Long> countByFavoritoTrue();

    @Aggregation("{ $group: { _id: null, mediaVisualizacoes: { $avg: '$visualizacao' } } }")
    Mono<Double> averageVisualizacao();

}
