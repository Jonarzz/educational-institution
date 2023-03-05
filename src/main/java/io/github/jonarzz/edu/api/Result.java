package io.github.jonarzz.edu.api;

public interface Result<T> {

    boolean isOk();

    T getSubject();

    String getMessage();

}
