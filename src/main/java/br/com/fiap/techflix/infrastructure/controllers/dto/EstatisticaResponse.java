package br.com.fiap.techflix.infrastructure.controllers.dto;

import lombok.Data;

@Data
public class EstatisticaResponse {
    private long totalVideos;
    private long videosFavoritados;
    private double mediaVisualizacoes;

    public EstatisticaResponse(long totalVideos, long videosFavoritados, double mediaVisualizacoes) {
        this.totalVideos = totalVideos;
        this.videosFavoritados = videosFavoritados;
        this.mediaVisualizacoes = mediaVisualizacoes;
    }
}