package io.github.jonarzz.edu.domain.professor;

import static io.github.jonarzz.edu.domain.professor.FakeProfessorConfiguration.DEFAULT_MAX_COURSES_COUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

import io.github.jonarzz.edu.api.result.Result;
import io.github.jonarzz.edu.domain.DomainInjector;
import io.github.jonarzz.edu.domain.FakeDomainInjector;
import io.github.jonarzz.edu.domain.common.FieldsOfStudy;
import io.github.jonarzz.edu.domain.common.PersonIdentification;
import io.github.jonarzz.edu.domain.course.CourseRepository;
import io.github.jonarzz.edu.domain.course.Views.CourseView;
import io.github.jonarzz.edu.domain.course.Views.FacultyCoursesView;
import io.github.jonarzz.edu.domain.faculty.FacultyId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CreateCourseCommandTest {

    DomainInjector injector = new FakeDomainInjector();
    ProfessorRepository professorRepository = injector.professorRepository();
    CourseRepository courseRepository = injector.courseRepository();

    @Test
    void successfullyHandleCreateCourseCommand() {
        var professorId = UUID.randomUUID();
        var fieldsOfStudy = FieldsOfStudy.from("math", "physics");
        var facultyId = new FacultyId(UUID.randomUUID(), "Faculty name");
        var command = new CreateCourseCommand(
                facultyId,
                professorId,
                "course name",
                fieldsOfStudy
        );
        professorRepository.saveNew(facultyId, new ProfessorView(
                professorId,
                new PersonIdentification("2515A551B"),
                fieldsOfStudy
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isTrue();
        assertThat(courseRepository.getFacultyCourses(facultyId))
                .returns(1, FacultyCoursesView::count);
    }

    @Test
    void handleCourseConfigConstraintViolation() {
        var professorId = UUID.randomUUID();
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var facultyId = new FacultyId(UUID.randomUUID(), "Faculty name");
        var command = new CreateCourseCommand(
                facultyId,
                professorId,
                "course name",
                fieldsOfStudy
        );

        var result = command.getHandler(injector)
                            .handle(command);

        assertFailedResult(result, "Courses should have");
        assertThat(courseRepository.getFacultyCourses(facultyId))
                .returns(0, FacultyCoursesView::count);
    }

    @Test
    void handleDomainError() {
        var professorId = UUID.randomUUID();
        var fieldsOfStudy = FieldsOfStudy.from("math", "physics");
        var facultyId = new FacultyId(UUID.randomUUID(), "Faculty name");
        var command = new CreateCourseCommand(
                facultyId,
                professorId,
                "course name",
                fieldsOfStudy
        );
        professorRepository.saveNew(facultyId, new ProfessorView(
                professorId,
                new PersonIdentification("2515A551B"),
                fieldsOfStudy,
                DEFAULT_MAX_COURSES_COUNT + 1,
                true
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertFailedResult(result, "Professor cannot lead");
        assertThat(courseRepository.getFacultyCourses(facultyId))
                .returns(0, FacultyCoursesView::count);
    }

    private static void assertFailedResult(Result<CourseView> result,
                                           String expectedMessageBeginning) {
        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isNotOk)
                .extracting(Result::getMessage, STRING)
                .startsWith(expectedMessageBeginning);
    }
}