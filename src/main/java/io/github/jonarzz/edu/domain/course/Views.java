package io.github.jonarzz.edu.domain.course;

import lombok.*;
import lombok.experimental.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

public class Views {

    private Views() {
    }

    public record CourseView(
            UUID id,
            String name,
            FieldsOfStudy fieldsOfStudy,
            UUID professorId
    ) {

        public CourseView {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Course name cannot be empty");
            }
        }

        public static CourseView newCourse(ValidCourseData courseData, UUID professorId) {
            return new CourseView(
                    null, courseData.name(), courseData.fieldsOfStudy(), professorId
            );
        }
    }

    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true)
    public static class FacultyCoursesView {

        Collection<CourseView> courses;

        public int count() {
            return courses.size();
        }
    }
}
