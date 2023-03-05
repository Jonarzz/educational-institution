package io.github.jonarzz.edu.domain.faculty;

public record FakeFacultyConfiguration(
        int minimumProfessorYearsOfExperience,
        int minimumNumberOfMatchingFieldsOfStudy
) implements FacultyConfiguration {

    static final int DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE = 5;
    static final int DEFAULT_MIN_NUMBER_OF_MATCHING_FIELDS_OF_STUDY = 1;

    public FakeFacultyConfiguration() {
        this(DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE,
             DEFAULT_MIN_NUMBER_OF_MATCHING_FIELDS_OF_STUDY);
    }
}
