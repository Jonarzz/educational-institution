package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;

public record CreateFacultyCommand(
        UUID educationalInstitutionId,
        String name,
        FieldsOfStudy fieldsOfStudy
) implements Command<FacultyInjector> {

    @Override
    @SuppressWarnings("unchecked")
    public CreateFacultyCommandHandler getHandler(FacultyInjector injector) {
        return new CreateFacultyCommandHandler(
                injector.educationalInstitutionRepository()
        );
    }
}