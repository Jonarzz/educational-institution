package io.github.jonarzz.edu.domain.course;

import static io.github.jonarzz.edu.domain.course.CourseView.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import io.github.jonarzz.edu.domain.common.*;

class CourseViewTest {

    @ParameterizedTest
    @CsvSource(value = {"''", "null"}, nullValues = "null")
    void shouldNotAllowEmptyCourseName(String value) {
        assertThatThrownBy(() -> newCourse(
                value,
                FieldsOfStudy.from("math")
        )).hasMessage("Course name cannot be empty");
    }

}