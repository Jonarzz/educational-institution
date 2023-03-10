package io.github.jonarzz.edu.api.result;

public interface FailedResult<T> extends Result<T> {

    @Override
    default boolean isOk() {
        return false;
    }

    @Override
    default T getSubject() {
        return null;
    }
}
