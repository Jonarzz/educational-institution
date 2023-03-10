package io.github.jonarzz.edu.api.result;

import java.util.*;

import io.github.jonarzz.edu.api.*;

public interface Result<T> {

    boolean isOk();

    T getSubject();

    String getMessage();

    default Optional<Event> toEvent() {
        return Optional.empty();
    }
}
