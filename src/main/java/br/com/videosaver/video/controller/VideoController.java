package br.com.videosaver.video.controller;

import br.com.videosaver.video.model.VideoInput;
import br.com.videosaver.video.model.VideoOutput;
import br.com.videosaver.video.service.VideoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/videos")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoController {

    VideoService videoService;

    @GetMapping
    public List<VideoOutput> listAll() {
        return videoService.listAll();
    }

    @GetMapping("/{id}")
    public VideoOutput retrieve(@PathVariable @NotNull UUID id) {
        return videoService.retrieve(id);
    }

    @PostMapping
    public ResponseEntity<VideoOutput> create(@RequestBody @Valid VideoInput input, UriComponentsBuilder uri) {
        VideoOutput videoOutput = videoService.create(input);
        URI address = uri.path("videos/{id}").buildAndExpand(videoOutput.getId()).toUri();
        return ResponseEntity.created(address).body(videoOutput);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoOutput> update(@RequestBody @Valid VideoInput input, @PathVariable @NotNull UUID id, UriComponentsBuilder uri) {
        VideoOutput videoOutput = videoService.update(id, input);
        URI address = uri.path("videos/{id}").buildAndExpand(videoOutput.getId()).toUri();
        return ResponseEntity.created(address).body(videoOutput);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull UUID id) {
        videoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
