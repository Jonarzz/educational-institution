package io.github.jonarzz.edu.domain.professor;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
public record ProfessorView(
        UUID id,
        PersonIdentification personIdentification,
        FieldsOfStudy fieldsOfStudy,
        int leadCoursesCount,
        boolean active
) {

    public ProfessorView {
        if (leadCoursesCount < 0) {
            throw new IllegalArgumentException("Number of courses lead by a professor cannot be negative");
        }
    }

    public ProfessorView(
            UUID id,
            PersonIdentification personIdentification,
            FieldsOfStudy fieldsOfStudy
    ) {
        this(id, personIdentification, fieldsOfStudy, 0, true);
    }

    public static ProfessorView newProfessor(
            PersonIdentification personIdentification,
            FieldsOfStudy fieldsOfStudy
    ) {
        return new ProfessorView(null, personIdentification, fieldsOfStudy);
    }

    public static ProfessorView inactive(ProfessorView subject) {
        return new ProfessorView(
                subject.id, subject.personIdentification, subject.fieldsOfStudy, subject.leadCoursesCount,
                false
        );
    }

    Professor toDomainObject(
            ProfessorConfiguration config,
            ProfessorResignationListener resignationListener
    ) {
        return new Professor(
                id,
                personIdentification,
                fieldsOfStudy,
                leadCoursesCount,
                active,
                config,
                resignationListener
        );
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ProfessorView other)) {
            return false;
        }
        return personIdentification.equals(other.personIdentification);
    }

    @Override
    public int hashCode() {
        return 31 * personIdentification.hashCode();
    }
}
