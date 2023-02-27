package io.github.jonarzz.edu.domain.common.result;

import io.github.jonarzz.edu.api.*;

public record NotFound<T>(
        String entityName,
        String identifyingField,
        Object fieldValue
) implements FailedResult<T> {

    @Override
    public String getMessage() {
        return "Not found %s with %s = %s".formatted(
                entityName.toLowerCase(), identifyingField, fieldValue);
    }
}
