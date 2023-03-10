package io.github.jonarzz.edu.domain.professor;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.FakeProfessorResignationListener.*;

class ProfessorTest {

    FakeProfessorResignationListener resignationListener = new FakeProfessorResignationListener();

    UUID professorId = UUID.randomUUID();
    PersonIdentification professorPersonalId = new PersonIdentification("5191A7519C");

    String resignationReason = "Found another job";

    @Test
    void resign() {
        var active = true;
        var professor = new Professor(professorId, professorPersonalId, active, resignationListener);

        var result = professor.resign(resignationReason);

        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject)
                .returns(professorId, ProfessorView::id)
                .returns(professorPersonalId, ProfessorView::personIdentification)
                .returns(false, ProfessorView::active)
                .satisfies(subject -> assertThat(resignationListener.events())
                        .singleElement()
                        .returns(subject, Event::professor)
                        .returns(resignationReason, Event::reason));
    }

    @Test
    void resign_professorInactive() {
        var active = false;
        var professor = new Professor(professorId, professorPersonalId, active, resignationListener);

        var result = professor.resign(resignationReason);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Not active professor cannot resign", Result::getMessage)
                .returns(null, Result::getSubject);
        assertThat(resignationListener.events())
                .isEmpty();
    }
}