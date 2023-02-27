package io.github.jonarzz.edu.api;

import java.util.*;

public interface Result<T> {

    boolean isOk();

    Optional<T> getSubject();

    String getMessage();

}
