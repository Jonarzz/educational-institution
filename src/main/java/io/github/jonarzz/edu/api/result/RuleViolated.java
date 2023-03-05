package io.github.jonarzz.edu.api.result;

import io.github.jonarzz.edu.api.*;

public record RuleViolated<T>(
        String message
) implements FailedResult<T> {

    @Override
    public String getMessage() {
        return message;
    }
}
