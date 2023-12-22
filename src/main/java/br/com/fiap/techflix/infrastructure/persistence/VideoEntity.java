package br.com.fiap.techflix.infrastructure.persistence;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "video")
@Getter
@Setter
@NoArgsConstructor
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
