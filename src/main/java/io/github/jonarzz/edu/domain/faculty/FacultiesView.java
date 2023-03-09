package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@ValueObject
public record FacultiesView(
        UUID educationalInstitutionId,
        Collection<FacultyView> faculties
) {

    Faculties toDomainObject() {
        return new Faculties(
                faculties
        );
    }
}
