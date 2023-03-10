package io.github.jonarzz.edu.domain.professor;

import static io.github.jonarzz.edu.domain.professor.ProfessorView.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;

class ProfessorResignationCommandTest {

    DomainInjector injector = new FakeDomainInjector();
    ProfessorRepository professorRepository = injector.professorRepository();

    @Test
    void successfullyHandleProfessorResignationCommand() {
        var facultyId = UUID.randomUUID();
        var professorId = UUID.randomUUID();
        var resignationReason = "Personal reasons";
        var command = new ProfessorResignationCommand(professorId, resignationReason);
        professorRepository.saveNew(facultyId, new ProfessorView(
                professorId,
                new PersonIdentification("2515A551B"),
                FieldsOfStudy.from("math")
        ));

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isTrue();
        assertThat(professorRepository.getByFacultyId(facultyId))
                .singleElement()
                .returns(professorId, ProfessorView::id)
                .returns(false, ProfessorView::active);
    }

    @Test
    void handleDomainErrorForProfessorResignationCommand() {
        var facultyId = UUID.randomUUID();
        var professorId = UUID.randomUUID();
        var active = false;
        var resignationReason = "Personal reasons";
        var command = new ProfessorResignationCommand(professorId, resignationReason);
        var professorBeforeHandling = inactive(new ProfessorView(
                professorId,
                new PersonIdentification("5021515AB35"),
                FieldsOfStudy.from("math")
        ));
        professorRepository.saveNew(facultyId, professorBeforeHandling);

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isFalse();
        assertThat(professorRepository.getByFacultyId(facultyId))
                .singleElement()
                .isEqualTo(professorBeforeHandling);
    }
}