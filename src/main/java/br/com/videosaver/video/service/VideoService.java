package br.com.videosaver.video.service;

import br.com.videosaver.category.model.CategoryEntity;
import br.com.videosaver.category.repository.CategoryRepository;
import br.com.videosaver.infra.exception.bundle.GeneralException;
import br.com.videosaver.infra.utils.GeneralMessages;
import br.com.videosaver.video.model.VideoInput;
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
    CategoryRepository categoryRepository;
    ModelMapper mapper;

    public List<VideoOutput> listAll() {
        List<VideoEntity> videoEntities = repository.findAll();
        return videoEntities.stream().map(video -> mapper.map(video, VideoOutput.class)).toList();
    }

    public List<VideoOutput> listVideosByCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new GeneralException(GeneralMessages.CATEGORY_NOT_FOUND);
        }
        List<VideoEntity> videoEntities = repository.findByCategoryId(categoryId);
        return videoEntities.stream().map(video -> mapper.map(video, VideoOutput.class)).toList();
    }

    public List<VideoOutput> listByTitle(String search) {
        List<VideoEntity> videoEntities = repository.findByTitleContaining(search);
        return videoEntities.stream().map(video -> mapper.map(video, VideoOutput.class)).toList();
    }

    public VideoOutput retrieve(UUID id) {
        return repository.findById(id)
                .map(video -> mapper.map(video, VideoOutput.class))
                .orElseThrow(() -> new GeneralException(GeneralMessages.VIDEO_NOT_FOUND));
    }

    public VideoOutput create(VideoInput input) {
        VideoEntity videoEntity = mapper.map(input, VideoEntity.class);
        CategoryEntity category;
        if (input.getCategory() != null) {
            category = categoryRepository.findById(input.getCategory()).orElseThrow(() -> new GeneralException(GeneralMessages.CATEGORY_NOT_FOUND));
        } else {
            category = categoryRepository.findById(UUID.fromString("d8accddd-8c70-451c-a71f-8881fdd259a8")).get();
        }
        videoEntity.setCategory(category);
        VideoEntity savedEntity = repository.save(videoEntity);
        return mapper.map(savedEntity, VideoOutput.class);
    }

    public VideoOutput update(UUID id, VideoInput input) {
        if (!repository.existsById(id)) {
            throw new GeneralException(GeneralMessages.VIDEO_NOT_FOUND);
        }
        VideoEntity videoEntity = mapper.map(input, VideoEntity.class);
        if (input.getCategory() != null) {
            CategoryEntity category = categoryRepository.findById(input.getCategory()).orElseThrow(() -> new GeneralException(GeneralMessages.CATEGORY_NOT_FOUND));
            videoEntity.setCategory(category);
        }
        videoEntity.setId(id);
        VideoEntity savedEntity = repository.save(videoEntity);
        return mapper.map(savedEntity, VideoOutput.class);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new GeneralException(GeneralMessages.VIDEO_NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
