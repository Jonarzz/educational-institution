package io.github.jonarzz.edu.domain.common;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.ValueObject;

@ValueObject
public record Vacancies(
        int count
) {

    public Vacancies {
        if (count <= 0) {
            throw new IllegalArgumentException("Vacancies count should be greater than 0");
        }
    }
}
