package br.com.videosaver.video.controller;

import br.com.videosaver.video.model.VideoOutput;
import br.com.videosaver.video.service.VideoService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
