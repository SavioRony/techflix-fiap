package br.com.fiap.techflix.controller;

import br.com.fiap.techflix.model.Video;
import br.com.fiap.techflix.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("videos")
public class VideoControler {

    @Autowired
    private VideoService videoService;


    @GetMapping(value = "{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable("title") String title, @RequestHeader("range") String range) {
        System.out.println("Range: " + range);
        return videoService.getVideo(title);
    }

    @PostMapping("/upload")
    public Mono<Video> uploadVideo(@RequestPart("titulo") String titulo, @RequestPart("fileToUpload") Mono<FilePart> filePartMono) {
        return videoService.uploadVideo(titulo, filePartMono);
    }

}
