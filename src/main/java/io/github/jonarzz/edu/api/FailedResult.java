package io.github.jonarzz.edu.api;

import java.util.*;

public interface FailedResult<T> extends Result<T> {

    @Override
    default boolean isOk() {
        return false;
    }

    @Override
    default Optional<T> getSubject() {
        return Optional.empty();
    }
}
