package io.github.jonarzz.edu.api;

public interface SuccessfulResult<T> extends Result<T> {

    @Override
    default boolean isOk() {
        return true;
    }

}
