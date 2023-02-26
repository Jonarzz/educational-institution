package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.common.result.RejectionReason.*;
import static io.vavr.control.Either.*;
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
class EducationalInstitution {

    Collection<Faculty> faculties;

    Either<RejectionReason, Faculty> createFaculty(String name, FieldsOfStudy fieldsOfStudy) {
        var facultyAlreadyExists = faculties.stream()
                                            .map(Faculty::name)
                                            .anyMatch(name::equals);
        if (facultyAlreadyExists) {
            return left(ALREADY_EXISTS);
        }
        var faculty = new Faculty(name, fieldsOfStudy);
        faculties.add(faculty);
        return right(faculty);
    }
}
