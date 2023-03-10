package io.github.jonarzz.edu.domain.faculty;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;

@AggregateRoot
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
final class Faculties {

    Collection<String> existingFacultyNames;

    Result<FacultyView> createFaculty(String name, FieldsOfStudy fieldsOfStudy,
                                      Vacancies maxProfessorVacancies,
                                      Vacancies maxStudentVacancies) {
        if (existingFacultyNames.contains(name)) {
            return new AlreadyExists<>("faculty", "name", name);
        }
        return new Created<>(FacultyView.newFaculty(
                name,
                fieldsOfStudy,
                maxProfessorVacancies,
                maxStudentVacancies
        ));
    }
}
