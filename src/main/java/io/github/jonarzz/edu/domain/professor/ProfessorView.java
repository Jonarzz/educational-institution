package io.github.jonarzz.edu.domain.professor;

import java.util.*;

public record ProfessorView(
        UUID id
        // TODO other fields
) {

    public ProfessorView() {
        this(null);
    }
}
