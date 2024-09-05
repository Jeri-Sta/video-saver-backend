package br.com.videosaver.video.validator;

import br.com.videosaver.infra.utils.GeneralMessages;
import br.com.videosaver.video.model.VideoEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class VideoValidatorTest {

    @InjectMocks
    VideoValidator videoValidator;

    EasyRandom easyRandom = new EasyRandom();

    @Nested
    class ValidateFieldsTest {
        @Test
        void testShouldThrowExceptionWithNullTitle() {
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            videoEntity.setTitle(null);

            assertThatThrownBy(() -> videoValidator.validateFields(videoEntity))
                    .hasMessage(String.format(GeneralMessages.EMPTY_FIELD, "título"));
        }

        @Test
        void testShouldThrowExceptionWithNullDescription() {
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            videoEntity.setDescription(null);

            assertThatThrownBy(() -> videoValidator.validateFields(videoEntity))
                    .hasMessage(String.format(GeneralMessages.EMPTY_FIELD, "descrição"));
        }

        @Test
        void testShouldThrowExceptionWithNullUrl() {
            VideoEntity videoEntity = easyRandom.nextObject(VideoEntity.class);
            videoEntity.setUrl(null);

            assertThatThrownBy(() -> videoValidator.validateFields(videoEntity))
                    .hasMessage(String.format(GeneralMessages.EMPTY_FIELD, "url"));
        }
    }
}