package io.github.jonarzz.edu.domain.common.result;

import io.github.jonarzz.edu.api.*;

public record Created<T>(
        T subject
) implements SuccessfulResult {

}
