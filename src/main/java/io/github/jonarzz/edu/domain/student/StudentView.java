package io.github.jonarzz.edu.domain.student;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

public record StudentView(
        UUID id,
        PersonIdentification personIdentification
) {

    public StudentView(PersonIdentification personIdentification) {
        this(null, personIdentification);
    }
}
