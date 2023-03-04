package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;

public record EmployProfessorCommand(
        UUID educationalInstitutionId,
        String facultyName,
        Candidate candidate
) implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public EmployProfessorCommandHandler getHandler(DomainInjector injector) {
        return new EmployProfessorCommandHandler(
                injector.facultyConfiguration(),
                injector.facultyRepository(),
                injector.professorRepository()
        );
    }
}
