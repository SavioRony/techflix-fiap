package br.com.fiap.techflix.main;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import br.com.fiap.techflix.application.usecases.VideoUseCase;
import br.com.fiap.techflix.infrastructure.gateways.VideoEntityMapper;
import br.com.fiap.techflix.infrastructure.gateways.VideoRepositoryGateway;
import br.com.fiap.techflix.infrastructure.persistence.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {VideoConfig.class, VideoEntityMapper.class})
@ExtendWith(SpringExtension.class)
class VideoConfigTest {
    @Autowired
    private VideoConfig videoConfig;

    @Autowired
    private VideoEntityMapper videoEntityMapper;

    @MockBean
    private VideoRepository videoRepository;

    @Test
    void testVideoUseCase() {

        VideoRepository videoRepository = mock(VideoRepository.class);
        VideoRepositoryGateway videoGateway = new VideoRepositoryGateway(videoRepository, new VideoEntityMapper());

        assertTrue(videoConfig.videoUseCase(videoGateway, new AnnotationConfigReactiveWebApplicationContext()) instanceof VideoUseCase);
    }

    @Test
    void testVideoGateway() {
        VideoRepository videoRepository = mock(VideoRepository.class);
        assertTrue(videoConfig.videoGateway(videoRepository, videoEntityMapper) instanceof VideoRepositoryGateway);
    }
}
