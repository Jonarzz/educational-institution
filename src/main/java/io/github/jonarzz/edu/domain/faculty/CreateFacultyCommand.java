package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;

public record CreateFacultyCommand(
        UUID educationalInstitutionId,
        String name,
        FieldsOfStudy fieldsOfStudy,
        Vacancies maxProfessorVacancies,
        Vacancies maxStudentVacancies
) implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public CreateFacultyCommandHandler getHandler(DomainInjector injector) {
        return new CreateFacultyCommandHandler(
                injector.facultyRepository()
        );
    }
}