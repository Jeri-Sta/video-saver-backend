package br.com.videosaver.video.service;

import br.com.videosaver.video.model.VideoEntity;
import br.com.videosaver.video.model.VideoOutput;
import br.com.videosaver.video.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoService {

    VideoRepository repository;
    ModelMapper mapper;

    public List<VideoOutput> listAll() {
        List<VideoEntity> videoEntities = repository.findAll();
        return videoEntities.stream().map(video -> mapper.map(video, VideoOutput.class)).toList();
    }
}
