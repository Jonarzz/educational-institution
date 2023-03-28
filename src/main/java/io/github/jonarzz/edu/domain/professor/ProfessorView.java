package io.github.jonarzz.edu.domain.professor;

import io.github.jonarzz.edu.domain.common.FieldsOfStudy;
import io.github.jonarzz.edu.domain.common.PersonIdentification;
import java.util.UUID;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.ValueObject;

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
        return new ProfessorView(UUID.randomUUID(), personIdentification, fieldsOfStudy);
    }

    public ProfessorView inactive() {
        return new ProfessorView(
                id, personIdentification, fieldsOfStudy, leadCoursesCount,
                false
        );
    }

    Professor toDomainObject(ProfessorConfiguration config) {
        return toDomainObject(config, null);
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
