package io.github.jonarzz.edu.domain.professor;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

class ProfessorViewTest {

    @Test
    void shouldNotAllowNegativeLeadCoursesCount() {
        assertThatThrownBy(() -> new ProfessorView(
                UUID.randomUUID(),
                new PersonIdentification("823A8125C"),
                FieldsOfStudy.from("math"),
                -1,
                true
        )).hasMessage("Number of courses lead by a professor cannot be negative");
    }
}