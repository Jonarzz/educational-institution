package io.github.jonarzz.edu.domain.professor;

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
class Professor {

    UUID id;
    PersonIdentification personIdentification;
    boolean active;

    ProfessorResignationListener resignationListener;

    Result<ProfessorView> resign(String reason) {
        if (!active) {
            return new RuleViolated<>("Not active professor cannot resign");
        }
        var inactiveProfessor = ProfessorView.inactive(toView());
        resignationListener.onProfessorResignation(inactiveProfessor, reason);
        return new Done<>(inactiveProfessor);
    }

    ProfessorView toView() {
        return new ProfessorView(
                id,
                personIdentification
        );
    }
}
