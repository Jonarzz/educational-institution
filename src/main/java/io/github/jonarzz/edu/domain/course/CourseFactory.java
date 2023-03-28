package io.github.jonarzz.edu.domain.course;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.course.Views.*;

@Factory
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CourseFactory {

    CourseConfiguration config;

    public Result<ValidCourseData> prepareCourse(FacultyCoursesView existingFacultyCourses,
                                                 String name, FieldsOfStudy fieldsOfStudy) {
        var existingCoursesCount = existingFacultyCourses.count();
        // greater than or equal instead of just equal to
        // because config might have been changed after courses creation
        if (existingCoursesCount >= config.maximumCoursesWithinFaculty()) {
            return new RuleViolated<>("Faculty has %s courses already, cannot create more"
                                              .formatted(existingCoursesCount));
        }
        if (fieldsOfStudy.count() < config.minimumFieldsOfStudy()
            || fieldsOfStudy.count() > config.maximumFieldsOfStudy()) {
            return new RuleViolated<>("Courses should have between %s and %s fields of study"
                                              .formatted(config.minimumFieldsOfStudy(),
                                                         config.maximumFieldsOfStudy()));
        }
        return new Created<>(
                new ValidCourseData(name, fieldsOfStudy)
        );
    }

}
