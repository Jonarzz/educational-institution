package io.github.jonarzz.edu.domain.professor;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

public record ProfessorView(
        UUID id,
        PersonalData personalData
) {

    public ProfessorView(PersonalData personalData) {
        this(null, personalData);
    }
}
