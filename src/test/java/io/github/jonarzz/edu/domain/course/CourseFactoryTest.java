package io.github.jonarzz.edu.domain.course;

import static io.github.jonarzz.edu.domain.common.TestFieldsOfStudyFactory.*;
import static io.github.jonarzz.edu.domain.course.FakeCourseConfiguration.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.course.Views.*;

class CourseFactoryTest {

    CourseFactory courseFactory = new CourseFactory(
            new FakeCourseConfiguration()
    );

    @ParameterizedTest
    @MethodSource("argSourceForValidTest")
    void prepareValidCourseData(int existingCoursesCount, int fieldsOfStudyCount) {
        var fieldsOfStudy = fieldsOfStudyFrom(
                IntStream.rangeClosed(1, fieldsOfStudyCount)
                         .mapToObj(i -> "name" + i)
        );
        var existingFacultyCourses = createExistingFacultyCourses(existingCoursesCount, fieldsOfStudy);
        var courseName = "course name";

        var result = courseFactory.prepareCourse(existingFacultyCourses, courseName, fieldsOfStudy);

        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject)
                .returns(courseName, ValidCourseData::name)
                .returns(fieldsOfStudy, ValidCourseData::fieldsOfStudy);
    }

    static Stream<Arguments> argSourceForValidTest() {
        return Stream.of(
                arguments(MAX_COURSES_WITHIN_FACULTY - 1, MIN_FIELDS_OF_STUDY),
                arguments(MAX_COURSES_WITHIN_FACULTY - 1, MIN_FIELDS_OF_STUDY + 1),
                arguments(MAX_COURSES_WITHIN_FACULTY - 1, MAX_FIELDS_OF_STUDY - 1),
                arguments(MAX_COURSES_WITHIN_FACULTY - 1, MAX_FIELDS_OF_STUDY)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {MAX_COURSES_WITHIN_FACULTY, MAX_COURSES_WITHIN_FACULTY + 1})
    void maximumCoursesWithinFacultyReached(int existingCoursesCount) {
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var existingFacultyCourses = createExistingFacultyCourses(existingCoursesCount, fieldsOfStudy);
        var courseName = "course name";

        var result = courseFactory.prepareCourse(existingFacultyCourses, courseName, fieldsOfStudy);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Faculty has %s courses already, cannot create more"
                                 .formatted(existingCoursesCount),
                         Result::getMessage)
                .returns(null, Result::getSubject);
    }

    @ParameterizedTest
    @ValueSource(ints = {MIN_FIELDS_OF_STUDY - 1, MAX_FIELDS_OF_STUDY + 1})
    void fieldsOfStudyCountOutOfAcceptableBounds(int fieldsOfStudyCount) {
        var fieldsOfStudy = fieldsOfStudyFrom(
                IntStream.rangeClosed(1, fieldsOfStudyCount)
                         .mapToObj(i -> "name" + i)
        );
        var existingFacultyCourses = new FacultyCoursesView(Set.of());
        var courseName = "course name";

        var result = courseFactory.prepareCourse(existingFacultyCourses, courseName, fieldsOfStudy);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Courses should have between %s and %s fields of study"
                                 .formatted(MIN_FIELDS_OF_STUDY, MAX_FIELDS_OF_STUDY),
                         Result::getMessage)
                .returns(null, Result::getSubject);
    }

    static FacultyCoursesView createExistingFacultyCourses(int count, FieldsOfStudy fieldsOfStudy) {
        return new FacultyCoursesView(
                IntStream.rangeClosed(1, count)
                         .mapToObj(i -> new CourseView(
                                 UUID.randomUUID(),
                                 "name" + i,
                                 fieldsOfStudy,
                                 UUID.randomUUID()
                         ))
                         .toList()
        );
    }
}