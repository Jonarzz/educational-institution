package io.github.jonarzz.edu.domain.course;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
public record CourseView(
        UUID id,
        String name,
        FieldsOfStudy fieldsOfStudy
) {

    public CourseView {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
    }

    public static CourseView newCourse(String name, FieldsOfStudy fieldsOfStudy) {
        return new CourseView(null, name, fieldsOfStudy);
    }

}
