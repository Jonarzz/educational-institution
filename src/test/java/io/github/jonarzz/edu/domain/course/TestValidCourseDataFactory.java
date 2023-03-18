package io.github.jonarzz.edu.domain.course;

import io.github.jonarzz.edu.domain.common.*;

public class TestValidCourseDataFactory {

    public static ValidCourseData create(String name, FieldsOfStudy fieldsOfStudy) {
        return new ValidCourseData(
                name, fieldsOfStudy
        );
    }

}
