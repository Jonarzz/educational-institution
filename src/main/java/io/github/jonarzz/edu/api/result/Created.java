package io.github.jonarzz.edu.api.result;

import io.github.jonarzz.edu.api.*;

public record Created<T>(
        T subject
) implements SuccessfulResult<T> {

    @Override
    public T getSubject() {
        return subject;
    }

    @Override
    public String getMessage() {
        return "Created " + subject;
    }
}
