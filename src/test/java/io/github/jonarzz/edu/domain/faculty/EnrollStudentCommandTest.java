package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.faculty.CandidateForStudent.*;
import io.github.jonarzz.edu.domain.faculty.Views.*;
import io.github.jonarzz.edu.domain.student.*;

class EnrollStudentCommandTest {

    static final PersonIdentification PERSONAL_DATA = new PersonIdentification("124912A04B");

    DomainInjector injector = new FakeDomainInjector();
    FacultyRepository facultyRepository = injector.facultyRepository();
    StudentRepository studentRepository = injector.studentRepository();

    @Test
    void successfullyHandleStudentEnrollmentCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "Mathematics";
        var fieldOfStudyName = "math";
        var fieldsOfStudy = FieldsOfStudy.from(fieldOfStudyName);
        var command = new EnrollStudentCommand(new FacultyId(institutionId, facultyName), new CandidateForStudent(
                Set.of(new TestResult(fieldOfStudyName, Score.fromPercentage(90))),
                PERSONAL_DATA
        ));
        var facultyId = facultyRepository.saveNew(institutionId, new NewFacultyView(
                facultyName, fieldsOfStudy, new Vacancies(1), new Vacancies(10)
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isTrue();
        assertThat(studentRepository.getByFacultyId(facultyId))
                .filteredOn(StudentView::personIdentification, PERSONAL_DATA)
                .as("New student")
                .hasSize(1);
    }

    @Test
    void handleDomainErrorForStudentEnrollmentCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "Mathematics";
        var fieldOfStudyName = "math";
        var fieldsOfStudy = FieldsOfStudy.from(fieldOfStudyName);
        var command = new EnrollStudentCommand(new FacultyId(institutionId, facultyName), new CandidateForStudent(
                Set.of(new TestResult(fieldOfStudyName, Score.fromPercentage(10))),
                PERSONAL_DATA
        ));
        var facultyId = facultyRepository.saveNew(institutionId, new NewFacultyView(
                facultyName, fieldsOfStudy, new Vacancies(1), new Vacancies(10)
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isFalse();
        assertThat(studentRepository.getByFacultyId(facultyId))
                .as("Enrolled students")
                .isEmpty();
    }
}