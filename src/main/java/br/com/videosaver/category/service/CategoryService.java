package br.com.videosaver.category.service;

import br.com.videosaver.category.model.CategoryEntity;
import br.com.videosaver.category.model.CategoryInput;
import br.com.videosaver.category.model.CategoryOutput;
import br.com.videosaver.category.repository.CategoryRepository;
import br.com.videosaver.infra.exception.bundle.GeneralException;
import br.com.videosaver.infra.validation.Validator;
import br.com.videosaver.video.model.VideoEntity;
import br.com.videosaver.video.model.VideoInput;
import br.com.videosaver.video.model.VideoOutput;
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
public class CategoryService {

    CategoryRepository repository;
    ModelMapper mapper;
    Validator<CategoryEntity> validator;

    public List<CategoryOutput> listAll() {
        List<CategoryEntity> categoryEntities = repository.findAll();
        return categoryEntities.stream().map(category -> mapper.map(category, CategoryOutput.class)).toList();
    }

    public CategoryOutput retrieve(UUID id) {
        return repository.findById(id)
                .map(category -> mapper.map(category, CategoryOutput.class))
                .orElseThrow(() -> new GeneralException("Categoria não encontrada"));
    }

    public CategoryOutput create(CategoryInput input) {
        CategoryEntity categoryEntity = mapper.map(input, CategoryEntity.class);
        validator.validateFields(categoryEntity);
        CategoryEntity savedEntity = repository.save(categoryEntity);
        return mapper.map(savedEntity, CategoryOutput.class);
    }

    public CategoryOutput update(UUID id, CategoryInput input) {
        if (!repository.existsById(id)) {
            throw new GeneralException("Categoria não encontrada");
        }
        CategoryEntity categoryEntity = mapper.map(input, CategoryEntity.class);
        categoryEntity.setId(id);
        validator.validateFields(categoryEntity);
        CategoryEntity savedEntity = repository.save(categoryEntity);
        return mapper.map(savedEntity, CategoryOutput.class);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new GeneralException("Categoria não encontrada");
        }
        repository.deleteById(id);
    }
}
