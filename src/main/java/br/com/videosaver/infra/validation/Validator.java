package br.com.videosaver.infra.validation;

import br.com.videosaver.infra.exception.bundle.GeneralException;

public interface Validator<T> {

    void validateFields(T entity) throws GeneralException;
}
