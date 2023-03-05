package io.github.jonarzz.edu.domain.professor;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

public record ProfessorView(
        UUID id,
        PersonIdentification personIdentification
) {

    public ProfessorView(PersonIdentification personIdentification) {
        this(null, personIdentification);
    }
}
