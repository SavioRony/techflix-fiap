package br.com.fiap.techflix.infrastructure.controllers;

import br.com.fiap.techflix.application.usecases.VideoUseCase;
import br.com.fiap.techflix.domain.entity.Video;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("videos")
public class VideoController {

    private final VideoUseCase videoUseCase;
    private static final String VIDEO_TYPE_FORMAT = "%s.mp4";
    private static final Path basePath = Paths.get("./src/main/resources/uploads");


    public VideoController(VideoUseCase videoUseCase) {
        this.videoUseCase = videoUseCase;
    }

    @GetMapping(value = "{id}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable("id") String id, @RequestHeader("range") String range) {
        return videoUseCase.getVideo(id, range);
    }

    @PostMapping("/upload")
    public Mono<Video> uploadVideo(@RequestPart("titulo") String titulo,
                                   @RequestPart("categoria") String categoria,
                                   @RequestPart("fileToUpload") Mono<FilePart> filePartMono) {
        Video video = new Video(titulo, categoria);
        return filePartMono
                .doOnNext(fp -> video.setNomeArquivo(fp.filename().replace(".mp4", "")))
                .flatMap(fp -> fp.transferTo(basePath.resolve(String.format(VIDEO_TYPE_FORMAT, video.getId()))))
                .then(videoUseCase.salvarVideo(video));
    }



}
