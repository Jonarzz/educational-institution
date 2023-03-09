package io.github.jonarzz.edu.domain.common;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

@ValueObject
public record PersonIdentification(
        // simplified - normally could be a pair of
        // an ID and the ID type (e.g. national ID, passport etc.)
        String nationalIdNumber
) {

    public PersonIdentification {
        if (nationalIdNumber == null || nationalIdNumber.isEmpty()) {
            throw new IllegalArgumentException("National ID number cannot be empty");
        }
    }
}
