package br.com.fiap.techflix.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "video")
@Data
@ToString
@AllArgsConstructor
public class VideoEntity {

    private UUID id;
    private String titulo;
    private String nomeArquivo;
    private LocalDateTime dataDeCadastro;
    private String path;
    private String categoria;
    private Integer visualizacao;
    private Boolean favorito;

}
