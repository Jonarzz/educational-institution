package io.github.jonarzz.edu.domain.professor;

import static io.github.jonarzz.edu.domain.professor.FakeProfessorConfiguration.DEFAULT_MAX_COURSES_COUNT;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.jonarzz.edu.api.result.Result;
import io.github.jonarzz.edu.domain.common.FieldsOfStudy;
import io.github.jonarzz.edu.domain.common.PersonIdentification;
import io.github.jonarzz.edu.domain.course.TestValidCourseDataFactory;
import io.github.jonarzz.edu.domain.course.Views;
import io.github.jonarzz.edu.domain.professor.FakeProfessorResignationListener.Event;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProfessorTest {

    ProfessorConfiguration config = new FakeProfessorConfiguration();
    FakeProfessorResignationListener resignationListener = new FakeProfessorResignationListener();

    UUID professorId = UUID.randomUUID();
    PersonIdentification professorPersonalId = new PersonIdentification("5191A7519C");
    FieldsOfStudy fieldsOfStudy = FieldsOfStudy.from("math");

    @Nested
    class ResignationTest {

        String resignationReason = "Found another job";

        @Test
        void success() {
            var professor = professor()
                    .fieldsOfStudy(fieldsOfStudy)
                    .create();

            var result = professor.resign(resignationReason);

            assertThat(result)
                    .as(result.toString())
                    .returns(true, Result::isOk)
                    .extracting(Result::getSubject)
                    .returns(professorId, ProfessorView::id)
                    .returns(professorPersonalId, ProfessorView::personIdentification)
                    .returns(false, ProfessorView::active)
                    .satisfies(subject -> assertThat(resignationListener.events())
                            .singleElement()
                            .returns(subject, Event::professor)
                            .returns(resignationReason, Event::reason));
        }

        @Test
        void failure_professorInactive() {
            var professor = professor()
                    .fieldsOfStudy(fieldsOfStudy)
                    .active(false)
                    .create();

            var result = professor.resign(resignationReason);

            assertThat(result)
                    .as(result.toString())
                    .returns(false, Result::isOk)
                    .returns("Not active professor cannot perform actions", Result::getMessage)
                    .returns(null, Result::getSubject);
            assertThat(resignationListener.events())
                    .isEmpty();
        }
    }

    @Nested
    class CourseCreationTest {

        @Test
        void success() {
            var professor = professor()
                    .fieldsOfStudy(fieldsOfStudy)
                    .create();
            var courseName = "Math 101";
            var courseData = TestValidCourseDataFactory.create(courseName, fieldsOfStudy);

            var result = professor.createCourse(courseData);

            assertThat(result)
                    .as(result.toString())
                    .returns(true, Result::isOk)
                    .extracting(Result::getSubject)
                    .returns(null, Views.CourseView::id)
                    .returns(courseName, Views.CourseView::name)
                    .returns(fieldsOfStudy, Views.CourseView::fieldsOfStudy);
        }

        @Test
        void failure_professorInactive() {
            var professor = professor()
                    .fieldsOfStudy(fieldsOfStudy)
                    .active(false)
                    .create();
            var courseName = "Math 101";
            var courseData = TestValidCourseDataFactory.create(courseName, fieldsOfStudy);

            var result = professor.createCourse(courseData);

            assertThat(result)
                    .as(result.toString())
                    .returns(false, Result::isOk)
                    .returns("Not active professor cannot perform actions", Result::getMessage);
        }

        @ParameterizedTest
        @ValueSource(ints = {DEFAULT_MAX_COURSES_COUNT, DEFAULT_MAX_COURSES_COUNT + 1})
        void failure_alreadyLeadsMaxNumberOfCourses(int ledCoursesCount) {
            var professor = professor()
                    .fieldsOfStudy(fieldsOfStudy)
                    .ledCoursesCount(ledCoursesCount)
                    .create();
            var courseName = "Math 101";
            var courseData = TestValidCourseDataFactory.create(courseName, fieldsOfStudy);

            var result = professor.createCourse(courseData);

            assertThat(result)
                    .as(result.toString())
                    .returns(false, Result::isOk)
                    .returns("Professor cannot lead more than " + DEFAULT_MAX_COURSES_COUNT + " courses",
                             Result::getMessage);
        }

        @Test
        void failure_notAllFieldsOfStudyMatch() {
            var professorFieldsOfStudy = FieldsOfStudy.from("math", "physics");
            var professor = professor()
                    .fieldsOfStudy(professorFieldsOfStudy)
                    .create();
            var courseName = "Quantum mechanics for dummies";
            var courseFieldsOfStudy = FieldsOfStudy.from("physics", "math", "quantum physics", "chemistry");
            var courseData = TestValidCourseDataFactory.create(courseName, courseFieldsOfStudy);

            var result = professor.createCourse(courseData);

            assertThat(result)
                    .as(result.toString())
                    .returns(false, Result::isOk)
                    .returns("Professor cannot create a course without required qualifications: chemistry, quantum physics",
                             Result::getMessage);
        }
    }

    @Builder(
            builderMethodName = "professor",
            buildMethodName = "create"
    )
    @SuppressWarnings("unused") // used by Lombok builder
    private Professor createProfessor(FieldsOfStudy fieldsOfStudy, int ledCoursesCount, Boolean active) {
        return new Professor(
                professorId, professorPersonalId,
                fieldsOfStudy, ledCoursesCount,
                Optional.ofNullable(active)
                        .orElse(true),
                config, resignationListener
        );
    }
}