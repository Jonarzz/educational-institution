package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

class FacultyCommandDispatcherTest {

    FacultyInjector injector = new FakeFacultyInjector();
    FacultyCommandDispatcher dispatcher = new FacultyCommandDispatcher(injector);

    @Test
    void successfullyHandleFacultyCreationCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "faculty name";
        var fieldsOfStudy = new FieldsOfStudy("test field of study");
        var command = new CreateFacultyCommand(institutionId, facultyName, fieldsOfStudy);
        injector.educationalInstitutionRepository()
                .save(new FacultiesView(
                        institutionId,
                        Set.of(new FacultyView(institutionId, "existing faculty", fieldsOfStudy))
                ));

        var result = dispatcher.handle(command);

        assertThat(result.isRight())
                .isTrue();
    }

    @Test
    void handleErrorForFacultyCreationCommand() {
        var institutionId = UUID.randomUUID();
        var facultyName = "faculty name";
        var fieldsOfStudy = new FieldsOfStudy("test field of study");
        var command = new CreateFacultyCommand(institutionId, facultyName, fieldsOfStudy);

        var result = dispatcher.handle(command);

        assertThat(result.isRight())
                .isFalse();
    }
}