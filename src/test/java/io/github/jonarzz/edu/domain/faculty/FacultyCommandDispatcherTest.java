package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;

class FacultyCommandDispatcherTest {

    DomainInjector injector = new FakeDomainInjector();
    SyncCommandDispatcher dispatcher = new SyncCommandDispatcher(injector);

    @Test
    void successfullyHandleFacultyCreationCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "faculty name";
        var fieldsOfStudy = FieldsOfStudy.from("test field of study");
        var maxProfessorVacancies = new Vacancies(1);
        var command = new CreateFacultyCommand(institutionId, facultyName, fieldsOfStudy, maxProfessorVacancies);
        injector.facultyRepository()
                .save(institutionId, new FacultyView(
                        institutionId, "existing faculty", fieldsOfStudy, Set.of(), maxProfessorVacancies
                ));

        var result = dispatcher.handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isTrue();
    }

    @Test
    void handleErrorForFacultyCreationCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "faculty name";
        var fieldsOfStudy = FieldsOfStudy.from("test field of study");
        var maxProfessorVacancies = new Vacancies(1);
        var command = new CreateFacultyCommand(institutionId, facultyName, fieldsOfStudy, maxProfessorVacancies);
        injector.facultyRepository()
                .save(institutionId, new FacultyView(
                        institutionId, facultyName, fieldsOfStudy, Set.of(), maxProfessorVacancies
                ));

        var result = dispatcher.handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isFalse();
    }
}