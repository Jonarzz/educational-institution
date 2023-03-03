package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.common.result.*;

@AggregateRoot
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class Faculties {

    Collection<FacultyView> existingFaculties;

    static Faculties fromView(FacultiesView view) {
        return new Faculties(view.faculties());
    }

    Result<FacultyView> createFaculty(String name, FieldsOfStudy fieldsOfStudy,
                                      Vacancies maxProfessorVacancies) {
        var facultyAlreadyExists = existingFaculties.stream()
                                                    .map(FacultyView::name)
                                                    .anyMatch(name::equals);
        if (facultyAlreadyExists) {
            return new AlreadyExists<>("faculty", "name", name);
        }
        var faculty = new NewFaculty(name, fieldsOfStudy, maxProfessorVacancies);
        return new Created<>(faculty.toView());
    }
}
