package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.faculty.FakeFacultyConfiguration.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

class EmployProfessorCommandTest {

    static final PersonIdentification PERSONAL_DATA = new PersonIdentification("124912A04B");

    DomainInjector injector = new FakeDomainInjector();
    FacultyRepository facultyRepository = injector.facultyRepository();
    ProfessorRepository professorRepository = injector.professorRepository();
    SyncCommandDispatcher dispatcher = new SyncCommandDispatcher(injector);

    @Test
    void successfullyHandleProfessorEmploymentCommand() {
        var institutionId = UUID.randomUUID();
        var facultyId = UUID.randomUUID();
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var command = new EmployProfessorCommand(institutionId, facultyName, new Candidate(
                DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE, fieldsOfStudy, PERSONAL_DATA
        ));
        facultyRepository.save(institutionId, new FacultyView(
                facultyId, facultyName, fieldsOfStudy, Set.of(), new Vacancies(1)
        ));

        var result = dispatcher.handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isTrue();
        assertThat(professorRepository.getByFacultyId(facultyId))
                .filteredOn(ProfessorView::personIdentification, PERSONAL_DATA)
                .as("New professor")
                .hasSize(1);
    }

    @Test
    void handleDomainErrorForProfessorEmploymentCommand() {
        var institutionId = UUID.randomUUID();
        var facultyId = UUID.randomUUID();
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var yearsOfExperience = DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE / 2;
        var command = new EmployProfessorCommand(institutionId, facultyName, new Candidate(
                yearsOfExperience, fieldsOfStudy, PERSONAL_DATA
        ));
        facultyRepository.save(institutionId, new FacultyView(
                facultyId, facultyName, fieldsOfStudy, Set.of(), new Vacancies(1)
        ));

        var result = dispatcher.handle(command);

        assertThat(result.isOk())
                .as(result.toString())
                .isFalse();
        assertThat(professorRepository.getByFacultyId(facultyId))
                .as("Employed professors")
                .isEmpty();
    }

    @Test
    void handleMissingFacultyForProfessorEmploymentCommand() {
        var institutionId = UUID.randomUUID();
        var facultyId = UUID.randomUUID();
        var facultyName = "Mathematics";
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var yearsOfExperience = DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE / 2;
        var command = new EmployProfessorCommand(institutionId, facultyName, new Candidate(
                yearsOfExperience, fieldsOfStudy, PERSONAL_DATA
        ));
        facultyRepository.save(institutionId, new FacultyView(
                facultyId, "Physics", fieldsOfStudy, Set.of(), new Vacancies(1)
        ));

        var result = dispatcher.handle(command);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Not found faculty with name '%s'".formatted(facultyName),
                         Result::getMessage);
        assertThat(professorRepository.getByFacultyId(facultyId))
                .as("Employed professors")
                .isEmpty();
    }
}