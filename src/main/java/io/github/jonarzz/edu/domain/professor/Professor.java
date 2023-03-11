package io.github.jonarzz.edu.domain.professor;

import static io.github.jonarzz.edu.domain.course.CourseView.*;
import static java.lang.String.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.course.*;

@AggregateRoot
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class Professor {

    UUID id;
    PersonIdentification personIdentification;
    FieldsOfStudy fieldsOfStudy;
    int leadCoursesCount;
    boolean active;

    ProfessorConfiguration config;
    ProfessorResignationListener resignationListener;

    Result<ProfessorView> resign(String reason) {
        if (!active) {
            return new RuleViolated<>("Not active professor cannot resign");
        }
        var inactiveProfessor = ProfessorView.inactive(toView());
        resignationListener.onProfessorResignation(inactiveProfessor, reason);
        return new Done<>(inactiveProfessor);
    }

    Result<CourseView> createCourse(String name, FieldsOfStudy fieldsOfStudy) {
        if (!active) {
            return new RuleViolated<>("Not active professor cannot create courses");
        }
        if (leadCoursesCount == config.maximumLeadCoursesCount()) {
            return new RuleViolated<>("Professor cannot lead more than " + leadCoursesCount + " courses");
        }
        var mismatchedFieldsOfStudy = fieldsOfStudy.diff(this.fieldsOfStudy);
        if (!mismatchedFieldsOfStudy.isEmpty()) {
            return new RuleViolated<>("Professor cannot create a course without required qualifications: "
                                      + join(", ", mismatchedFieldsOfStudy));
        }
        // TODO add faculty-related rules
        return new Created<>(newCourse(name, fieldsOfStudy));
    }

    private ProfessorView toView() {
        return new ProfessorView(
                id,
                personIdentification,
                fieldsOfStudy,
                leadCoursesCount,
                active
        );
    }
}
