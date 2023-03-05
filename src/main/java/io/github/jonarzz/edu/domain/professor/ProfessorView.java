package io.github.jonarzz.edu.domain.professor;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
public record ProfessorView(
        UUID id,
        PersonIdentification personIdentification
) {

    public ProfessorView(PersonIdentification personIdentification) {
        this(null, personIdentification);
    }
}
