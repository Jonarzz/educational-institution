package io.github.jonarzz.edu.api.result;

public interface SuccessfulResult<T> extends Result<T> {

    @Override
    default boolean isOk() {
        return true;
    }

}
