package br.com.videosaver.category.controller;

import br.com.videosaver.category.model.CategoryInput;
import br.com.videosaver.category.model.CategoryOutput;
import br.com.videosaver.category.service.CategoryService;
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

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    CategoryController categoryController;
    @Mock
    CategoryService categoryService;
    @Mock
    VideoService videoService;

    EasyRandom easyRandom = new EasyRandom();

    @Nested
    class ListAllTest {
        @Test
        void testShouldReturnListOfCategories() {
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);

            when(categoryService.listAll()).thenReturn(List.of(categoryOutput));

            List<CategoryOutput> outputs = categoryController.listAll();

            assertThat(outputs.get(0)).isEqualTo(categoryOutput);

            verify(categoryService).listAll();
            verifyNoMoreInteractions(categoryService);
        }

        @Test
        void testShouldReturnEmptyListOfCategories() {

            when(categoryService.listAll()).thenReturn(List.of());

            List<CategoryOutput> outputs = categoryController.listAll();

            assertThat(outputs).isEmpty();

            verify(categoryService).listAll();
            verifyNoMoreInteractions(categoryService);
        }
    }

    @Nested
    class RetrieveTest {
        @Test
        void testShouldReturnCategory() {
            UUID id = UUID.randomUUID();
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);

            when(categoryService.retrieve(id)).thenReturn(categoryOutput);

            CategoryOutput output = categoryController.retrieve(id);

            assertThat(output).isEqualTo(categoryOutput);

            verify(categoryService).retrieve(id);
            verifyNoMoreInteractions(categoryService);
        }

        @Test
        void testShouldNotReturnCategory() {
            UUID id = UUID.randomUUID();

            when(categoryService.retrieve(id)).thenReturn(null);

            CategoryOutput output = categoryController.retrieve(id);

            assertThat(output).isNull();

            verify(categoryService).retrieve(id);
            verifyNoMoreInteractions(categoryService);
        }
    }

    @Nested
    class ListVideosByCategoryTest {
        @Test
        void testShouldReturnListOfVideos() {
            UUID id = UUID.randomUUID();
            VideoOutput videoOutput = easyRandom.nextObject(VideoOutput.class);

            when(videoService.listVideosByCategory(id)).thenReturn(List.of(videoOutput));

            List<VideoOutput> outputs = categoryController.listVideosByCategory(id);

            assertThat(outputs.get(0)).isEqualTo(videoOutput);

            verify(videoService).listVideosByCategory(id);
            verifyNoMoreInteractions(videoService);
        }

        @Test
        void testShouldReturnEmptyListOfVideos() {
            UUID id = UUID.randomUUID();

            when(videoService.listVideosByCategory(id)).thenReturn(List.of());

            List<VideoOutput> outputs = categoryController.listVideosByCategory(id);

            assertThat(outputs).isEmpty();

            verify(videoService).listVideosByCategory(id);
            verifyNoMoreInteractions(videoService);
        }
    }

    @Nested
    class CreateTest {
        @Test
        void testShouldCreate() {
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);
            CategoryInput categoryInput = easyRandom.nextObject(CategoryInput.class);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

            when(categoryService.create(categoryInput)).thenReturn(categoryOutput);

            ResponseEntity<CategoryOutput> outputResponseEntity = categoryController.create(categoryInput, uriComponentsBuilder);

            assertThat(outputResponseEntity.getBody()).isEqualTo(categoryOutput);

            verify(categoryService).create(categoryInput);
            verifyNoMoreInteractions(categoryService);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void testShouldUpdate() {
            UUID id = UUID.randomUUID();
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);
            CategoryInput categoryInput = easyRandom.nextObject(CategoryInput.class);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

            when(categoryService.update(id, categoryInput)).thenReturn(categoryOutput);

            ResponseEntity<CategoryOutput> outputResponseEntity = categoryController.update(categoryInput, id, uriComponentsBuilder);

            assertThat(outputResponseEntity.getBody()).isEqualTo(categoryOutput);

            verify(categoryService).update(id, categoryInput);
            verifyNoMoreInteractions(categoryService);
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void testShouldDelete() {
            UUID id = UUID.randomUUID();

            ResponseEntity<HttpStatus> output = categoryController.delete(id);

            assertThat(output.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(categoryService).delete(id);
            verifyNoMoreInteractions(categoryService);
        }
    }

}