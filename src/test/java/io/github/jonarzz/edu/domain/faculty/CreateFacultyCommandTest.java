package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;

class CreateFacultyCommandTest {

    DomainInjector injector = new FakeDomainInjector();
    FacultyRepository facultyRepository = injector.facultyRepository();

    @Test
    void successfullyHandleFacultyCreationCommand() {
        var institutionId = UUID.randomUUID();
        var newFacultyName = "faculty name";
        var fieldsOfStudy = FieldsOfStudy.from("test field of study");
        var maxProfessorVacancies = new Vacancies(1);
        var maxStudentVacancies = new Vacancies(30);
        var command = new CreateFacultyCommand(institutionId, newFacultyName, fieldsOfStudy,
                                               maxProfessorVacancies, maxStudentVacancies);
        facultyRepository.create(institutionId, new FacultyView(
                institutionId, "existing faculty", fieldsOfStudy, Set.of(), maxProfessorVacancies, Set.of(), maxStudentVacancies
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isTrue();
        assertThat(facultyRepository.getByEducationalInstitutionId(institutionId)
                                    .faculties())
                .filteredOn(FacultyView::name, newFacultyName)
                .as("New faculty")
                .hasSize(1);
    }

    @Test
    void handleErrorForFacultyCreationCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "faculty name";
        var fieldsOfStudy = FieldsOfStudy.from("test field of study");
        var maxProfessorVacancies = new Vacancies(1);
        var maxStudentVacancies = new Vacancies(50);
        var command = new CreateFacultyCommand(institutionId, facultyName, fieldsOfStudy,
                                               maxProfessorVacancies, maxStudentVacancies);
        facultyRepository.create(institutionId, new FacultyView(
                institutionId, facultyName, fieldsOfStudy, Set.of(), maxProfessorVacancies, Set.of(), maxStudentVacancies
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isFalse();
        assertThat(facultyRepository.getByEducationalInstitutionId(institutionId)
                                    .faculties())
                .as("All faculties")
                .hasSize(1);
    }
}