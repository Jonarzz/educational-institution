package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.common.result.*;
import io.github.jonarzz.edu.domain.professor.*;

@AggregateRoot
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
class Faculty {

    final UUID id;
    final String name;
    final FieldsOfStudy fieldsOfStudy;

    FacultyConfiguration config;

    Faculty(String name, FieldsOfStudy fieldsOfStudy) {
        this(null, name, fieldsOfStudy);
    }

    Result<ProfessorView> employ(Candidate candidate) {
        var candidateExperience = candidate.yearsOfExperience();
        var minExperience = config.minimumProfessorYearsOfExperience();
        if (candidateExperience < minExperience) {
            return new RuleViolated<>("Candidate has %d years of experience, while minimum required is %d"
                                              .formatted(candidateExperience, minExperience));
        }
        // TODO rest of the rules
        return new Created<>(new ProfessorView());
    }

    FacultyView toView() {
        return new FacultyView(
                id,
                name,
                fieldsOfStudy
        );
    }

    Faculty withConfiguration(FacultyConfiguration config) {
        this.config = config;
        return this;
    }
}
