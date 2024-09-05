package br.com.videosaver.category.validator;

import br.com.videosaver.category.model.CategoryEntity;
import br.com.videosaver.infra.utils.GeneralMessages;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CategoryValidatorTest {

    @InjectMocks
    CategoryValidator categoryValidator;

    @Nested
    class ValidateFieldsTest {
        @Test
        void testShouldThrowExceptionWithNullTitle() {
            CategoryEntity categoryEntity = new CategoryEntity(UUID.randomUUID(), null, "teste");

            assertThatThrownBy(() -> categoryValidator.validateFields(categoryEntity))
                    .hasMessage(String.format(GeneralMessages.EMPTY_FIELD, "título"));
        }

        @Test
        void testShouldThrowExceptionWithNullColor() {
            CategoryEntity categoryEntity = new CategoryEntity(UUID.randomUUID(), "teste", null);

            assertThatThrownBy(() -> categoryValidator.validateFields(categoryEntity))
                    .hasMessage(String.format(GeneralMessages.EMPTY_FIELD, "cor"));
        }
    }
}