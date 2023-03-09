package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;

public record EnrollStudentCommand(
        UUID educationalInstitutionId,
        String facultyName,
        CandidateForStudent candidate
) implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public EnrollStudentCommandHandler getHandler(DomainInjector injector) {
        return new EnrollStudentCommandHandler(
                injector.facultyConfiguration(),
                injector.facultyRepository(),
                injector.studentRepository()
        );
    }
}
