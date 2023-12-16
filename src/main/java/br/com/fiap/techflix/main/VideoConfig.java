package br.com.fiap.techflix.main;

import br.com.fiap.techflix.application.gateway.VideoGateway;
import br.com.fiap.techflix.application.usecases.VideoUseCase;
import br.com.fiap.techflix.infrastructure.gateways.VideoEntityMapper;
import br.com.fiap.techflix.infrastructure.gateways.VideoRepositoryGateway;
import br.com.fiap.techflix.repository.VideoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class VideoConfig {

    @Bean
    VideoUseCase videoUseCase(VideoGateway videoGateway, ResourceLoader resourceLoader){
        return new VideoUseCase(videoGateway, resourceLoader);
    }

    @Bean
    VideoGateway videoGateway(VideoRepository videoRepository, VideoEntityMapper videoEntityMapper){
        return new VideoRepositoryGateway(videoRepository, videoEntityMapper);
    }
}
