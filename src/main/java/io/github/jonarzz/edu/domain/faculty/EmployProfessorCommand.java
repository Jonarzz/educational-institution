package io.github.jonarzz.edu.domain.faculty;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;

public record EmployProfessorCommand(
        FacultyId facultyId,
        CandidateForProfessor candidate
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
