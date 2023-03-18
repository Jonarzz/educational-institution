package io.github.jonarzz.edu.domain.course;

import static io.github.jonarzz.edu.domain.course.Views.CourseView.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

class CourseViewTest {

    @ParameterizedTest
    @CsvSource(value = {"''", "null"}, nullValues = "null")
    void shouldNotAllowEmptyCourseName(String courseName) {
        assertThatThrownBy(() -> newCourse(
                new ValidCourseData(
                        courseName,
                        FieldsOfStudy.from("math")
                ),
                UUID.randomUUID()
        )).hasMessage("Course name cannot be empty");
    }

}