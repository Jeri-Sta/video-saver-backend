package br.com.videosaver.video.service;

import br.com.videosaver.infra.exception.bundle.GeneralException;
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
import java.util.UUID;

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

    public VideoOutput retrieve(UUID id) {
        return repository.findById(id)
                .map(video -> mapper.map(video, VideoOutput.class))
                .orElseThrow(() -> new GeneralException("Vídeo não encontrado"));
    }
}
