package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

class FacultiesTest {

    @Test
    void createFaculty() {
        var faculties = new Faculties(Set.of());
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var maxProfessorVacancies = new Vacancies(1);
        var maxStudentVacancies = new Vacancies(15);

        var result = faculties.createFaculty(facultyName, fieldsOfStudy,
                                             maxProfessorVacancies, maxStudentVacancies);

        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject)
                .returns(facultyName, FacultyView::name)
                .returns(fieldsOfStudy, FacultyView::fieldsOfStudy);
    }

    @Test
    void tryToCreateFacultyThatAlreadyExists() {
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var employedProfessors = Set.<ProfessorView>of();
        var maxProfessorVacancies = new Vacancies(1);
        var maxStudentVacancies = new Vacancies(15);
        var enrolledStudents = Set.<StudentView>of();
        var faculties = new Faculties(Set.of(
                new FacultyView(
                        UUID.randomUUID(),
                        facultyName,
                        fieldsOfStudy,
                        employedProfessors,
                        maxProfessorVacancies,
                        enrolledStudents,
                        maxStudentVacancies
                )
        ));

        var result = faculties.createFaculty(facultyName, fieldsOfStudy,
                                             maxProfessorVacancies, maxStudentVacancies);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Faculty with name '%s' already exists"
                                 .formatted(facultyName),
                         Result::getMessage);
    }
}