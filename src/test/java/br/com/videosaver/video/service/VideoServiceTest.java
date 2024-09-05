package br.com.videosaver.video.service;

import br.com.videosaver.category.model.CategoryEntity;
import br.com.videosaver.category.repository.CategoryRepository;
import br.com.videosaver.infra.utils.GeneralMessages;
import br.com.videosaver.video.model.VideoEntity;
import br.com.videosaver.video.model.VideoInput;
import br.com.videosaver.video.model.VideoOutput;
import br.com.videosaver.video.repository.VideoRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @InjectMocks
    VideoService videoService;
    @Mock
    VideoRepository videoRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ModelMapper mapper;

    EasyRandom easyRandom = new EasyRandom();

    @Nested
    class ListAllTest {
        @Test
        void testShouldReturnListOfVideos() {
            List<VideoEntity> videoEntities = easyRandom.objects(VideoEntity.class, 2).toList();
            List<VideoOutput> videoOutputs = easyRandom.objects(VideoOutput.class, 2).toList();

            when(videoRepository.findAll()).thenReturn(videoEntities);
            when(mapper.map(videoEntities.get(0), VideoOutput.class)).thenReturn(videoOutputs.get(0));
            when(mapper.map(videoEntities.get(1), VideoOutput.class)).thenReturn(videoOutputs.get(1));

            List<VideoOutput> outputs = videoService.listAll();

            assertThat(outputs).isNotEmpty();
            assertThat(outputs.get(0)).isEqualTo(videoOutputs.get(0));
            assertThat(outputs.get(1)).isEqualTo(videoOutputs.get(1));

            verify(videoRepository).findAll();
            verify(mapper).map(videoEntities.get(0), VideoOutput.class);
            verify(mapper).map(videoEntities.get(1), VideoOutput.class);
            verifyNoMoreInteractions(videoRepository, mapper);
        }

        @Test
        void testShouldReturnEmptyListOfVideos() {
            when(videoRepository.findAll()).thenReturn(List.of());

            List<VideoOutput> outputs = videoService.listAll();

            assertThat(outputs).isEmpty();

            verify(videoRepository).findAll();
            verifyNoMoreInteractions(videoRepository, mapper);
        }
    }

    @Nested
    class RetrieveTest {
        @Test
        void testShouldReturnOneVideo() {
            UUID id = UUID.randomUUID();
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoRepository.findById(id)).thenReturn(Optional.of(videoEntity));
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            VideoOutput output = videoService.retrieve(id);

            assertThat(output).isEqualTo(videoOutput);

            verify(videoRepository).findById(id);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(videoRepository, mapper);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();

            when(videoRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> videoService.retrieve(id)).hasMessage(GeneralMessages.VIDEO_NOT_FOUND);

            verify(videoRepository).findById(id);
            verifyNoMoreInteractions(videoRepository, mapper);
        }
    }

    @Nested
    class CreateTest {
        @Test
        void testShouldCreateWithCategory() {
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(mapper.map(videoInput, VideoEntity.class)).thenReturn(videoEntity);
            when(categoryRepository.findById(videoInput.getCategory())).thenReturn(Optional.of(easyRandom.nextObject(CategoryEntity.class)));
            when(videoRepository.save(videoEntity)).thenReturn(videoEntity);
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            VideoOutput output = videoService.create(videoInput);

            assertThat(output).isEqualTo(videoOutput);

            verify(mapper).map(videoInput, VideoEntity.class);
            verify(categoryRepository).findById(videoInput.getCategory());
            verify(videoRepository).save(videoEntity);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(mapper, videoRepository);
        }

        @Test
        void testShouldCreateWithoutCategory() {
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            videoInput.setCategory(null);
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(mapper.map(videoInput, VideoEntity.class)).thenReturn(videoEntity);
            when(categoryRepository.findById(UUID.fromString("d8accddd-8c70-451c-a71f-8881fdd259a8"))).thenReturn(Optional.of(easyRandom.nextObject(CategoryEntity.class)));
            when(videoRepository.save(videoEntity)).thenReturn(videoEntity);
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            VideoOutput output = videoService.create(videoInput);

            assertThat(output).isEqualTo(videoOutput);

            verify(mapper).map(videoInput, VideoEntity.class);
            verify(categoryRepository).findById(UUID.fromString("d8accddd-8c70-451c-a71f-8881fdd259a8"));
            verify(videoRepository).save(videoEntity);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(mapper, videoRepository);
        }

        @Test
        void testShouldThrowGeneralException() {
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);

            when(mapper.map(videoInput, VideoEntity.class)).thenReturn(videoEntity);
            when(categoryRepository.findById(videoInput.getCategory())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> videoService.create(videoInput)).hasMessage(GeneralMessages.CATEGORY_NOT_FOUND);

            verify(mapper).map(videoInput, VideoEntity.class);
            verify(categoryRepository).findById(videoInput.getCategory());
            verifyNoMoreInteractions(mapper, categoryRepository);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void testShouldUpdateWithoutCategory() {
            UUID id = UUID.randomUUID();
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            videoInput.setCategory(null);
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoRepository.existsById(id)).thenReturn(true);
            when(mapper.map(videoInput, VideoEntity.class)).thenReturn(videoEntity);
            when(videoRepository.save(videoEntity)).thenReturn(videoEntity);
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            VideoOutput output = videoService.update(id, videoInput);

            assertThat(output).isEqualTo(videoOutput);

            verify(videoRepository).existsById(id);
            verify(mapper).map(videoInput, VideoEntity.class);
            verify(videoRepository).save(videoEntity);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(videoRepository, mapper);
        }

        @Test
        void testShouldUpdateWithCategory() {
            UUID id = UUID.randomUUID();
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoRepository.existsById(id)).thenReturn(true);
            when(mapper.map(videoInput, VideoEntity.class)).thenReturn(videoEntity);
            when(categoryRepository.findById(videoInput.getCategory())).thenReturn(Optional.of(easyRandom.nextObject(CategoryEntity.class)));
            when(videoRepository.save(videoEntity)).thenReturn(videoEntity);
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            VideoOutput output = videoService.update(id, videoInput);

            assertThat(output).isEqualTo(videoOutput);

            verify(videoRepository).existsById(id);
            verify(mapper).map(videoInput, VideoEntity.class);
            verify(categoryRepository).findById(videoInput.getCategory());
            verify(videoRepository).save(videoEntity);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(videoRepository, mapper);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();
            VideoInput categoryInput = easyRandom.nextObject(VideoInput.class);

            when(videoRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> videoService.update(id, categoryInput)).hasMessage(GeneralMessages.VIDEO_NOT_FOUND);

            verify(videoRepository).existsById(id);
            verifyNoMoreInteractions(videoRepository);
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void testShouldDelete() {
            UUID id = UUID.randomUUID();

            when(videoRepository.existsById(id)).thenReturn(true);

            videoService.delete(id);

            verify(videoRepository).existsById(id);
            verify(videoRepository).deleteById(id);
            verifyNoMoreInteractions(videoRepository);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();

            when(videoRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> videoService.delete(id)).hasMessage(GeneralMessages.VIDEO_NOT_FOUND);

            verify(videoRepository).existsById(id);
            verifyNoMoreInteractions(videoRepository);
        }
    }

    @Nested
    class ListByTitleTest {
        @Test
        void testShouldReturnVideo() {
            String search = "teste";
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoRepository.findByTitleContaining(search)).thenReturn(List.of(videoEntity));
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            List<VideoOutput> outputs = videoService.listByTitle(search);

            assertThat(outputs.get(0)).isEqualTo(videoOutput);

            verify(videoRepository).findByTitleContaining(search);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(videoRepository, mapper);
        }

        @Test
        void testShouldNotReturnVideo() {
            String search = "teste";

            when(videoRepository.findByTitleContaining(search)).thenReturn(List.of());

            List<VideoOutput> outputs = videoService.listByTitle(search);

            assertThat(outputs).isEmpty();

            verify(videoRepository).findByTitleContaining(search);
            verifyNoMoreInteractions(videoRepository, mapper);
        }
    }

    @Nested
    class ListVideosByCategoryTest {
        @Test
        void testShouldReturnVideo() {
            UUID id = UUID.randomUUID();
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(categoryRepository.existsById(id)).thenReturn(true);
            when(videoRepository.findByCategoryId(id)).thenReturn(List.of(videoEntity));
            when(mapper.map(videoEntity, VideoOutput.class)).thenReturn(videoOutput);

            List<VideoOutput> outputs = videoService.listVideosByCategory(id);

            assertThat(outputs.get(0)).isEqualTo(videoOutput);

            verify(categoryRepository).existsById(id);
            verify(videoRepository).findByCategoryId(id);
            verify(mapper).map(videoEntity, VideoOutput.class);
            verifyNoMoreInteractions(videoRepository, categoryRepository, mapper);
        }

        @Test
        void testShouldNotReturnVideo() {
            UUID id = UUID.randomUUID();

            when(categoryRepository.existsById(id)).thenReturn(true);
            when(videoRepository.findByCategoryId(id)).thenReturn(List.of());

            List<VideoOutput> outputs = videoService.listVideosByCategory(id);

            assertThat(outputs).isEmpty();

            verify(categoryRepository).existsById(id);
            verify(videoRepository).findByCategoryId(id);
            verifyNoMoreInteractions(videoRepository, categoryRepository);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();

            when(categoryRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> videoService.listVideosByCategory(id)).hasMessage(GeneralMessages.CATEGORY_NOT_FOUND);

            verify(categoryRepository).existsById(id);
            verifyNoMoreInteractions(categoryRepository);
        }
    }
}