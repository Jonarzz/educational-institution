package io.github.jonarzz.edu.domain.common.result;

import java.util.*;

import io.github.jonarzz.edu.api.*;

public record Created<T>(
        T subject
) implements SuccessfulResult<T> {

    @Override
    public Optional<T> getSubject() {
        return Optional.of(subject);
    }

    @Override
    public String getMessage() {
        return "Created " + subject;
    }
}
