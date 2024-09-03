package br.com.videosaver.category.validator;

import br.com.videosaver.category.model.CategoryEntity;
import br.com.videosaver.infra.exception.bundle.EmptyFieldException;
import br.com.videosaver.infra.exception.bundle.GeneralException;
import br.com.videosaver.infra.utils.GeneralMessages;
import br.com.videosaver.infra.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator implements Validator<CategoryEntity> {

    @Override
    public void validateFields(CategoryEntity entity) throws GeneralException {
        if (entity.getTitle() == null) {
            throw new EmptyFieldException(String.format(GeneralMessages.EMPTY_FIELD, "título"));
        } else if (entity.getColor() == null) {
            throw new EmptyFieldException(String.format(GeneralMessages.EMPTY_FIELD, "cor"));
        }
    }
}
