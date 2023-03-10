package io.github.jonarzz.edu.domain.faculty;

import static java.util.stream.Collectors.*;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@ValueObject
public record FacultiesView(
        UUID educationalInstitutionId,
        Collection<FacultyView> faculties
) {

    Faculties toDomainObject() {
        return new Faculties(
                faculties.stream()
                         .map(FacultyView::name)
                         .collect(toSet())
        );
    }
}
