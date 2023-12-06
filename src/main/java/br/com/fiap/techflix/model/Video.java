package br.com.fiap.techflix.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "video")
@Data
@ToString
public class Video {

    private UUID id;
    private String titulo;
    private String nomeArquivo;
    private LocalDateTime dataDeCadastro;
    private String path;
    private String categoria;
    private Integer visualizacao;
    private Boolean favorito;


    public Video(String titulo, String categoria) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.categoria = categoria;
        this.path = "./src/main/resources/uploads/" + this.id + ".mp4";
        this.dataDeCadastro = LocalDateTime.now();
        this.visualizacao = 0;
        this.favorito = false;
    }
}
