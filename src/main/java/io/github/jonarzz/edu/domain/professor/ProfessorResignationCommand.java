package io.github.jonarzz.edu.domain.professor;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;

public record ProfessorResignationCommand(
        UUID professorId,
        String resignationReason
) implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public ProfessorResignationCommandHandler getHandler(DomainInjector injector) {
        return new ProfessorResignationCommandHandler(
                injector.professorRepository(),
                injector.professorResignationListener()
        );
    }
}
