package io.github.jonarzz.edu.domain.professor;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jonarzz.edu.domain.DomainInjector;
import io.github.jonarzz.edu.domain.FakeDomainInjector;
import io.github.jonarzz.edu.domain.common.FieldsOfStudy;
import io.github.jonarzz.edu.domain.common.PersonIdentification;
import io.github.jonarzz.edu.domain.faculty.FacultyId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProfessorResignationCommandTest {

    DomainInjector injector = new FakeDomainInjector();
    ProfessorRepository professorRepository = injector.professorRepository();

    @Test
    void successfullyHandleProfessorResignationCommand() {
        var facultyId = new FacultyId(UUID.randomUUID(), "Faculty name");
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
        assertThat(professorRepository.getBy(facultyId, professorId))
                .returns(professorId, ProfessorView::id)
                .returns(false, ProfessorView::active);
    }

    @Test
    void handleDomainErrorForProfessorResignationCommand() {
        var facultyId = new FacultyId(UUID.randomUUID(), "Faculty name");
        var professorId = UUID.randomUUID();
        var active = false;
        var resignationReason = "Personal reasons";
        var command = new ProfessorResignationCommand(professorId, resignationReason);
        var professorBeforeHandling = new ProfessorView(
                professorId,
                new PersonIdentification("5021515AB35"),
                FieldsOfStudy.from("math")
        ).inactive();
        professorRepository.saveNew(facultyId, professorBeforeHandling);

        var result = command.getHandler(injector)
                            .handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isFalse();
        assertThat(professorRepository.getByFaculty(facultyId))
                .singleElement()
                .isEqualTo(professorBeforeHandling);
    }
}