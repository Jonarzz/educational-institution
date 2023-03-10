package io.github.jonarzz.edu.api.result;

public record RuleViolated<T>(
        String message
) implements FailedResult<T> {

    @Override
    public String getMessage() {
        return message;
    }
}
