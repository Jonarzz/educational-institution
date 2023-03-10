package io.github.jonarzz.edu.api.result;

public record Done<T>(
        T subject
) implements SuccessfulResult<T> {

    @Override
    public T getSubject() {
        return subject;
    }

    @Override
    public String getMessage() {
        return "Action done on " + subject;
    }
}
