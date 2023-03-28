package io.github.jonarzz.edu.domain.professor;

import static io.github.jonarzz.edu.domain.course.Views.CourseView.newCourse;
import static java.lang.String.join;

import io.github.jonarzz.edu.api.result.Created;
import io.github.jonarzz.edu.api.result.Done;
import io.github.jonarzz.edu.api.result.Result;
import io.github.jonarzz.edu.api.result.RuleViolated;
import io.github.jonarzz.edu.domain.common.FieldsOfStudy;
import io.github.jonarzz.edu.domain.common.PersonIdentification;
import io.github.jonarzz.edu.domain.course.ValidCourseData;
import io.github.jonarzz.edu.domain.course.Views.CourseView;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.AggregateRoot;

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

    // TODO divide into 2 objects? ProfessorResignation.process
    Result<ProfessorView> resign(String reason) {
        if (resignationListener == null) {
            throw new IllegalStateException("Cannot handle professor resignation without resignation listener");
        }
        return onActive(() -> {
            var inactiveProfessor = toView().inactive();
            resignationListener.onProfessorResignation(inactiveProfessor, reason);
            return new Done<>(inactiveProfessor);
        });
    }

    // TODO ProfessorLeadCourse.create
    Result<CourseView> createCourse(ValidCourseData courseData) {
        return onActive(() -> {
            int maxLedCourses = config.maximumLedCoursesCount();
            // greater than or equal instead of just equal to
            // because config might have been changed after courses creation
            if (leadCoursesCount >= maxLedCourses) {
                return new RuleViolated<>(
                        "Professor cannot lead more than " + maxLedCourses + " courses"
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

    private <R> Result<R> onActive(Supplier<Result<R>> action) {
        if (!active) {
            return new RuleViolated<>("Not active professor cannot perform actions");
        }
        return action.get();
    }
}
