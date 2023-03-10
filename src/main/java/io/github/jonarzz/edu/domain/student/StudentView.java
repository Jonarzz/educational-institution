package io.github.jonarzz.edu.domain.student;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

public record StudentView(
        UUID id,
        PersonIdentification personIdentification,
        boolean active
) {

    public StudentView(UUID id, PersonIdentification personIdentification) {
        this(id, personIdentification, true);
    }

    public static StudentView newStudent(PersonIdentification personIdentification) {
        return new StudentView(null, personIdentification);
    }

    public static StudentView inactive(StudentView subject) {
        return new StudentView(subject.id, subject.personIdentification, false);
    }
}
