package io.github.jonarzz.edu.domain.professor;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
public record ProfessorView(
        UUID id,
        PersonIdentification personIdentification,
        boolean active
) {

    public ProfessorView(UUID id, PersonIdentification personIdentification) {
        this(id, personIdentification, true);
    }

    public static ProfessorView newProfessor(PersonIdentification personIdentification) {
        return new ProfessorView(null, personIdentification);
    }

    public static ProfessorView inactive(ProfessorView subject) {
        return new ProfessorView(subject.id, subject.personIdentification, false);
    }

    Professor toDomainObject(ProfessorResignationListener resignationListener) {
        return new Professor(
                id,
                personIdentification,
                active,
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
