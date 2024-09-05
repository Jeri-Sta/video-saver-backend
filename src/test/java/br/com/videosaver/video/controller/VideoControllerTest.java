package br.com.videosaver.video.controller;

import br.com.videosaver.video.model.VideoInput;
import br.com.videosaver.video.model.VideoOutput;
import br.com.videosaver.video.service.VideoService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    @InjectMocks
    VideoController videoController;
    @Mock
    VideoService videoService;

    EasyRandom easyRandom = new EasyRandom();

    @Nested
    class ListAllTest {
        @Test
        void testShouldReturnListOfVideos() {
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoService.listAll()).thenReturn(List.of(videoOutput));

            List<VideoOutput> outputs = videoController.listAll();

            assertThat(outputs.get(0)).isEqualTo(videoOutput);

            verify(videoService).listAll();
            verifyNoMoreInteractions(videoService);
        }

        @Test
        void testShouldReturnEmptyListOfVideos() {

            when(videoService.listAll()).thenReturn(List.of());

            List<VideoOutput> outputs = videoController.listAll();

            assertThat(outputs).isEmpty();

            verify(videoService).listAll();
            verifyNoMoreInteractions(videoService);
        }
    }

    @Nested
    class RetrieveTest {
        @Test
        void testShouldReturnVideo() {
            UUID id = UUID.randomUUID();
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoService.retrieve(id)).thenReturn(videoOutput);

            VideoOutput output = videoController.retrieve(id);

            assertThat(output).isEqualTo(videoOutput);

            verify(videoService).retrieve(id);
            verifyNoMoreInteractions(videoService);
        }

        @Test
        void testShouldNotReturnVideo() {
            UUID id = UUID.randomUUID();

            when(videoService.retrieve(id)).thenReturn(null);

            VideoOutput output = videoController.retrieve(id);

            assertThat(output).isNull();

            verify(videoService).retrieve(id);
            verifyNoMoreInteractions(videoService);
        }
    }

    @Nested
    class ListByTitleTest {
        @Test
        void testShouldReturnVideo() {
            String search = "search";
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoService.listByTitle(search)).thenReturn(List.of(videoOutput));

            List<VideoOutput> output = videoController.listByTitle(search);

            assertThat(output.get(0)).isEqualTo(videoOutput);

            verify(videoService).listByTitle(search);
            verifyNoMoreInteractions(videoService);
        }

        @Test
        void testShouldNotReturnVideo() {
            String search = "search";

            when(videoService.listByTitle(search)).thenReturn(List.of());

            List<VideoOutput> output = videoController.listByTitle(search);

            assertThat(output).isEmpty();

            verify(videoService).listByTitle(search);
            verifyNoMoreInteractions(videoService);
        }
    }

    @Nested
    class CreateTest {
        @Test
        void testShouldCreate() {
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

            when(videoService.create(videoInput)).thenReturn(videoOutput);

            ResponseEntity<VideoOutput> outputResponseEntity = videoController.create(videoInput, uriComponentsBuilder);

            assertThat(outputResponseEntity.getBody()).isEqualTo(videoOutput);

            verify(videoService).create(videoInput);
            verifyNoMoreInteractions(videoService);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void testShouldUpdate() {
            UUID id = UUID.randomUUID();
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);
            VideoInput videoInput = easyRandom.nextObject(VideoInput.class);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

            when(videoService.update(id, videoInput)).thenReturn(videoOutput);

            ResponseEntity<VideoOutput> outputResponseEntity = videoController.update(videoInput, id, uriComponentsBuilder);

            assertThat(outputResponseEntity.getBody()).isEqualTo(videoOutput);

            verify(videoService).update(id, videoInput);
            verifyNoMoreInteractions(videoService);
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void testShouldDelete() {
            UUID id = UUID.randomUUID();

            ResponseEntity<HttpStatus> output = videoController.delete(id);

            assertThat(output.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(videoService).delete(id);
            verifyNoMoreInteractions(videoService);
        }
    }

}