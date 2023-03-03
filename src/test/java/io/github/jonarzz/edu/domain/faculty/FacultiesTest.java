package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

class FacultiesTest {

    @Test
    void createFaculty() {
        var faculties = new Faculties(Set.of());
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var maxProfessorVacancies = new Vacancies(1);

        var result = faculties.createFaculty(facultyName, fieldsOfStudy, maxProfessorVacancies);

        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject, optional(FacultyView.class))
                .get()
                .returns(facultyName, FacultyView::name)
                .returns(fieldsOfStudy, FacultyView::fieldsOfStudy);
    }

    @Test
    void tryToCreateFacultyThatAlreadyExists() {
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var maxProfessorVacancies = new Vacancies(1);
        var employedProfessors = Set.<ProfessorView>of();
        var faculties = new Faculties(Set.of(
                new FacultyView(
                        UUID.randomUUID(),
                        facultyName,
                        fieldsOfStudy,
                        employedProfessors,
                        maxProfessorVacancies
                )
        ));

        var result = faculties.createFaculty(facultyName, fieldsOfStudy, maxProfessorVacancies);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Faculty with name '%s' already exists"
                                 .formatted(facultyName),
                         Result::getMessage);
    }
}