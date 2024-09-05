package br.com.videosaver.category.service;

import br.com.videosaver.category.model.CategoryEntity;
import br.com.videosaver.category.model.CategoryInput;
import br.com.videosaver.category.model.CategoryOutput;
import br.com.videosaver.category.repository.CategoryRepository;
import br.com.videosaver.infra.utils.GeneralMessages;
import br.com.videosaver.infra.validation.Validator;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ModelMapper mapper;
    @Mock
    Validator<CategoryEntity> validator;

    EasyRandom easyRandom = new EasyRandom();

    @Nested
    class ListAllTest {
        @Test
        void testShouldReturnListOfCategories() {
            List<CategoryEntity> categoryEntities = easyRandom.objects(CategoryEntity.class, 2).toList();
            List<CategoryOutput> categoryOutputs = easyRandom.objects(CategoryOutput.class, 2).toList();

            when(categoryRepository.findAll()).thenReturn(categoryEntities);
            when(mapper.map(categoryEntities.get(0), CategoryOutput.class)).thenReturn(categoryOutputs.get(0));
            when(mapper.map(categoryEntities.get(1), CategoryOutput.class)).thenReturn(categoryOutputs.get(1));

            List<CategoryOutput> outputs = categoryService.listAll();

            assertThat(outputs).isNotEmpty();
            assertThat(outputs.get(0)).isEqualTo(categoryOutputs.get(0));
            assertThat(outputs.get(1)).isEqualTo(categoryOutputs.get(1));

            verify(categoryRepository).findAll();
            verify(mapper).map(categoryEntities.get(0), CategoryOutput.class);
            verify(mapper).map(categoryEntities.get(1), CategoryOutput.class);
            verifyNoMoreInteractions(categoryRepository, mapper);
        }

        @Test
        void testShouldReturnEmptyListOfCategories() {
            when(categoryRepository.findAll()).thenReturn(List.of());

            List<CategoryOutput> outputs = categoryService.listAll();

            assertThat(outputs).isEmpty();

            verify(categoryRepository).findAll();
            verifyNoMoreInteractions(categoryRepository, mapper);
        }
    }

    @Nested
    class RetrieveTest {
        @Test
        void testShouldReturnOneCategory() {
            UUID id = UUID.randomUUID();
            CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);

            when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryEntity));
            when(mapper.map(categoryEntity, CategoryOutput.class)).thenReturn(categoryOutput);

            CategoryOutput output = categoryService.retrieve(id);

            assertThat(output).isEqualTo(categoryOutput);

            verify(categoryRepository).findById(id);
            verify(mapper).map(categoryEntity, CategoryOutput.class);
            verifyNoMoreInteractions(categoryRepository, mapper);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();

            when(categoryRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoryService.retrieve(id)).hasMessage(GeneralMessages.CATEGORY_NOT_FOUND);

            verify(categoryRepository).findById(id);
            verifyNoMoreInteractions(categoryRepository, mapper);
        }
    }

    @Nested
    class CreateTest {
        @Test
        void testShouldCreate() {
            CategoryInput categoryInput = easyRandom.nextObject(CategoryInput.class);
            CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);

            when(mapper.map(categoryInput, CategoryEntity.class)).thenReturn(categoryEntity);
            when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
            when(mapper.map(categoryEntity, CategoryOutput.class)).thenReturn(categoryOutput);

            CategoryOutput output = categoryService.create(categoryInput);

            assertThat(output).isEqualTo(categoryOutput);

            verify(mapper).map(categoryInput, CategoryEntity.class);
            verify(categoryRepository).save(categoryEntity);
            verify(mapper).map(categoryEntity, CategoryOutput.class);
            verifyNoMoreInteractions(mapper, categoryRepository);
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void testShouldUpdate() {
            UUID id = UUID.randomUUID();
            CategoryInput categoryInput = easyRandom.nextObject(CategoryInput.class);
            CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);
            CategoryOutput categoryOutput = easyRandom.nextObject(CategoryOutput.class);

            when(categoryRepository.existsById(id)).thenReturn(true);
            when(mapper.map(categoryInput, CategoryEntity.class)).thenReturn(categoryEntity);
            when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
            when(mapper.map(categoryEntity, CategoryOutput.class)).thenReturn(categoryOutput);

            CategoryOutput output = categoryService.update(id, categoryInput);

            assertThat(output).isEqualTo(categoryOutput);

            verify(categoryRepository).existsById(id);
            verify(mapper).map(categoryInput, CategoryEntity.class);
            verify(categoryRepository).save(categoryEntity);
            verify(mapper).map(categoryEntity, CategoryOutput.class);
            verifyNoMoreInteractions(categoryRepository, mapper);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();
            CategoryInput categoryInput = easyRandom.nextObject(CategoryInput.class);

            when(categoryRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> categoryService.update(id, categoryInput)).hasMessage(GeneralMessages.CATEGORY_NOT_FOUND);

            verify(categoryRepository).existsById(id);
            verifyNoMoreInteractions(categoryRepository);
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void testShouldDelete() {
            UUID id = UUID.randomUUID();

            when(categoryRepository.existsById(id)).thenReturn(true);

            categoryService.delete(id);

            verify(categoryRepository).existsById(id);
            verify(categoryRepository).deleteById(id);
            verifyNoMoreInteractions(categoryRepository);
        }

        @Test
        void testShouldThrowGeneralException() {
            UUID id = UUID.randomUUID();

            when(categoryRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> categoryService.delete(id)).hasMessage(GeneralMessages.CATEGORY_NOT_FOUND);

            verify(categoryRepository).existsById(id);
            verifyNoMoreInteractions(categoryRepository);
        }
    }

}