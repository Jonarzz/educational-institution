package io.github.jonarzz.edu.domain.professor;

public record FakeProfessorConfiguration(
        int maximumLedCoursesCount
) implements ProfessorConfiguration {

    static final int DEFAULT_MAX_COURSES_COUNT = 3;

    public FakeProfessorConfiguration() {
        this(DEFAULT_MAX_COURSES_COUNT);
    }
}
