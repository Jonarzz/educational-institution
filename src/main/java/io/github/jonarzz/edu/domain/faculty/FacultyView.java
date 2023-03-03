package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

public record FacultyView(
        UUID id,
        String name,
        FieldsOfStudy fieldsOfStudy,
        Collection<ProfessorView> employedProfessors,
        Vacancies maxProfessorVacancies
) {

    Faculty toDomainObject(FacultyConfiguration config) {
        return new Faculty(
                id,
                name,
                fieldsOfStudy,
                employedProfessors,
                maxProfessorVacancies,
                config
        );
    }

}
