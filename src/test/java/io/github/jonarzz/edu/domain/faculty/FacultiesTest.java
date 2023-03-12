package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.faculty.Views.*;

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
                .returns(facultyName, NewFacultyView::name)
                .returns(fieldsOfStudy, NewFacultyView::fieldsOfStudy);
    }

    @Test
    void tryToCreateFacultyThatAlreadyExists() {
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var maxProfessorVacancies = new Vacancies(1);
        var maxStudentVacancies = new Vacancies(15);
        var faculties = new Faculties(Set.of(facultyName));

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