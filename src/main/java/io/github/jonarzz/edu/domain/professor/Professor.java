package io.github.jonarzz.edu.domain.professor;

import static io.github.jonarzz.edu.domain.course.Views.CourseView.*;
import static java.lang.String.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.function.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.course.*;
import io.github.jonarzz.edu.domain.course.Views.*;

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
        return new OnActiveProfessor<ProfessorView>()
                .act(() -> {
                    var inactiveProfessor = ProfessorView.inactive(toView());
                    resignationListener.onProfessorResignation(inactiveProfessor, reason);
                    return new Done<>(inactiveProfessor);
                });
    }

    Result<CourseView> createCourse(ValidCourseData courseData) {
        return new OnActiveProfessor<CourseView>()
                .act(() -> {
                    if (leadCoursesCount == config.maximumLeadCoursesCount()) {
                        return new RuleViolated<>(
                                "Professor cannot lead more than " + leadCoursesCount + " courses"
                        );
                    }
                    var mismatchedFieldsOfStudy = courseData.fieldsOfStudy()
                                                            .diff(fieldsOfStudy);
                    if (!mismatchedFieldsOfStudy.isEmpty()) {
                        return new RuleViolated<>(
                                "Professor cannot create a course without required qualifications: "
                                + join(", ", mismatchedFieldsOfStudy)
                        );
                    }
                    return new Created<>(newCourse(courseData, id));
                });
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

    private class OnActiveProfessor<R> {

        private Result<R> act(Supplier<Result<R>> action) {
            if (!active) {
                return new RuleViolated<>("Not active professor cannot perform actions");
            }
            return action.get();
        }
    }
}
