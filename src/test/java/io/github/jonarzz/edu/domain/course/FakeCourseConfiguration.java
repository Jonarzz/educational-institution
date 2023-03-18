package io.github.jonarzz.edu.domain.course;

public record FakeCourseConfiguration(
        int maximumCoursesWithinFaculty,
        int minimumFieldsOfStudy,
        int maximumFieldsOfStudy
) implements CourseConfiguration {

    public static final int MAX_COURSES_WITHIN_FACULTY = 3;
    public static final int MIN_FIELDS_OF_STUDY = 2;
    public static final int MAX_FIELDS_OF_STUDY = 5;

    public FakeCourseConfiguration() {
        this(MAX_COURSES_WITHIN_FACULTY,
             MIN_FIELDS_OF_STUDY,
             MAX_FIELDS_OF_STUDY);
    }
}
