package io.github.jonarzz.edu.api;

import java.util.*;

public interface Result<T> {

    boolean isOk();

    T getSubject();

    String getMessage();

    default Optional<Event> toEvent() {
        return Optional.empty();
    }
}
