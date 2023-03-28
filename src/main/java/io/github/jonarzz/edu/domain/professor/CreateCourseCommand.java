package io.github.jonarzz.edu.domain.professor;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.faculty.*;

public record CreateCourseCommand(
        FacultyId facultyId,
        UUID professorId,
        String courseName,
        FieldsOfStudy fieldsOfStudy
) implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public CreateCourseCommandHandler getHandler(DomainInjector injector) {
        return new CreateCourseCommandHandler(
                injector.courseRepository(),
                injector.courseConfiguration(),
                injector.professorRepository(),
                injector.professorConfiguration()
        );
    }
}
