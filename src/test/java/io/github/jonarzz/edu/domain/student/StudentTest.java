package io.github.jonarzz.edu.domain.student;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.student.FakeStudentResignationListener.Event;

class StudentTest {

    FakeStudentResignationListener resignationListener = new FakeStudentResignationListener();

    UUID studentId = UUID.randomUUID();
    PersonIdentification studentPersonalId = new PersonIdentification("5191A7519C");

    String resignationReason = "Not enough funds";

    @Test
    void resign() {
        var active = true;
        var student = new Student(studentId, studentPersonalId, active, resignationListener);

        var result = student.resign(resignationReason);

        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject)
                .returns(studentId, StudentView::id)
                .returns(studentPersonalId, StudentView::personIdentification)
                .returns(false, StudentView::active)
                .satisfies(subject -> assertThat(resignationListener.events())
                        .singleElement()
                        .returns(subject, Event::student)
                        .returns(resignationReason, Event::reason));
    }

    @Test
    void resign_studentInactive() {
        var active = false;
        var student = new Student(studentId, studentPersonalId, active, resignationListener);

        var result = student.resign(resignationReason);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Not active student cannot resign", Result::getMessage)
                .returns(null, Result::getSubject);
        assertThat(resignationListener.events())
                .isEmpty();
    }
}