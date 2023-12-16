package br.com.fiap.techflix.domain.dto;

import lombok.Data;

@Data
public class Estatisticas {
    private long totalVideos;
    private long videosFavoritados;
    private double mediaVisualizacoes;

    public Estatisticas(long totalVideos, long videosFavoritados, double mediaVisualizacoes) {
        this.totalVideos = totalVideos;
        this.videosFavoritados = videosFavoritados;
        this.mediaVisualizacoes = mediaVisualizacoes;
    }
}