package br.com.videosaver.video.validator;

import br.com.videosaver.infra.exception.bundle.EmptyFieldException;
import br.com.videosaver.infra.exception.bundle.GeneralException;
import br.com.videosaver.infra.utils.GeneralMessages;
import br.com.videosaver.infra.validation.Validator;
import br.com.videosaver.video.model.VideoEntity;
import org.springframework.stereotype.Component;

@Component
public class VideoValidator implements Validator<VideoEntity> {

    @Override
    public void validateFields(VideoEntity entity) throws GeneralException {
        if (entity.getTitle() == null) {
            throw new EmptyFieldException(String.format(GeneralMessages.EMPTY_FIELD, "título"));
        } else if (entity.getDescription() == null) {
            throw new EmptyFieldException(String.format(GeneralMessages.EMPTY_FIELD, "descrição"));
        } else if (entity.getUrl() == null) {
            throw new EmptyFieldException(String.format(GeneralMessages.EMPTY_FIELD, "url"));
        }
    }
}
