package io.github.jonarzz.edu.api.result;

public interface SuccessfulResult<T> extends Result<T> {

    @Override
    default boolean isOk() {
        return true;
    }

    @Override
    default <M> Result<M> mapFailure() {
        throw new IllegalStateException("Result is successful - cannot map as failure");
    }

}
