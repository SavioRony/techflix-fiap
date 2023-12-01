package br.com.fiap.techflix.service;

import br.com.fiap.techflix.model.Video;
import br.com.fiap.techflix.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoService {

    @Autowired
    private ResourceLoader resourceLoader;
    private static final String PATH_FORMAT = "classpath:uploads//%s.mp4";
    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");

    @Autowired
    private VideoRepository videoRepository;

    public Mono<Resource> getVideo(String title) {
        Resource resource = this.resourceLoader.getResource(String.format(PATH_FORMAT, title));
        return Mono.fromSupplier(() -> resource);
    }

    public Mono<Video> uploadVideo(String titulo, Mono<FilePart> filePartMono) {
        Video video = new Video(titulo);
        return filePartMono
                .doOnNext(fp -> video.setNomeArquivo(fp.filename().replace(".mp4", "")))
                .flatMap(fp -> fp.transferTo(basePath.resolve(String.format(VIDEO_TYPE_FORMAT, video.getId()))))
                .then(Mono.defer(() -> videoRepository.save(video)));
    }

    public Flux<Video> getVideos() {
        return videoRepository.findAll();
    }
}

