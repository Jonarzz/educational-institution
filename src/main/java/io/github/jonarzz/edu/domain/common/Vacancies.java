package io.github.jonarzz.edu.domain.common;

public record Vacancies(
        int count
) {

    public Vacancies {
        if (count <= 0) {
            throw new IllegalArgumentException("Vacancies count should be greater than 0");
        }
    }
}
