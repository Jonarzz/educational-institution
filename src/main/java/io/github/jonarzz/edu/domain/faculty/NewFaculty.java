package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@Entity
@RequiredArgsConstructor
@FieldDefaults(level = PACKAGE, makeFinal = true)
sealed class NewFaculty permits Faculty {

    String name;
    FieldsOfStudy fieldsOfStudy;
    Vacancies maxProfessorVacancies;

    FacultyView toView() {
        return new FacultyView(
                null,
                name,
                fieldsOfStudy,
                Set.of(),
                maxProfessorVacancies
        );
    }
}
