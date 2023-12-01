package br.com.fiap.techflix.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "video")
@Data
@ToString
public class Video {

    private UUID id;
    private String titulo;
    private String nomeArquivo;
    private String path;
    private Integer curtidas;
    private Integer visualizacao;


    public Video(String titulo) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.path = "./src/main/resources/uploads/"+ this.id +".mp4";
        this.curtidas = 0;
        this.visualizacao = 0;
    }
}
