package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.common.result.RejectionReason.*;
import static io.vavr.control.Either.*;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;

import io.vavr.control.*;
import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.common.result.*;

@AggregateRoot
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class Faculties {

    UUID educationalInstitutionId;
    Collection<Faculty> existingFaculties;

    static Faculties fromView(FacultiesView view) {
        return new Faculties(
                view.educationalInstitutionId(),
                view.faculties()
                    .stream()
                    .map(Faculty::fromView)
                    .collect(toSet())
        );
    }

    Either<RejectionReason, Faculty> createFaculty(String name, FieldsOfStudy fieldsOfStudy) {
        var facultyAlreadyExists = existingFaculties.stream()
                                                    .map(Faculty::name)
                                                    .anyMatch(name::equals);
        if (facultyAlreadyExists) {
            return left(ALREADY_EXISTS);
        }
        var faculty = new Faculty(name, fieldsOfStudy);
        existingFaculties.add(faculty);
        return right(faculty);
    }

    FacultiesView toView() {
        return new FacultiesView(
                educationalInstitutionId,
                existingFaculties.stream()
                                 .map(Faculty::toView)
                                 .collect(toUnmodifiableSet())
        );
    }
}
