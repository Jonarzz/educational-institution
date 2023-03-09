package io.github.jonarzz.edu.domain.student;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;

@AggregateRoot
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class Student {

    UUID id;
    PersonIdentification personIdentification;
    boolean active;

    StudentResignationListener resignationListener;

    // TODO add command and handler
    Result<StudentView> resign(String reason) {
        if (!active) {
            return new RuleViolated<>("Not active student cannot resign");
        }
        var inactiveStudent = StudentView.inactive(toView());
        resignationListener.onStudentResignation(inactiveStudent, reason);
        return new Done<>(inactiveStudent);
    }

    StudentView toView() {
        return new StudentView(
                id,
                personIdentification
        );
    }
}
