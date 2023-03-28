package io.github.jonarzz.edu.domain.professor;

import io.github.jonarzz.edu.api.CommandHandler;
import io.github.jonarzz.edu.api.result.Result;
import io.github.jonarzz.edu.domain.course.CourseConfiguration;
import io.github.jonarzz.edu.domain.course.CourseFactory;
import io.github.jonarzz.edu.domain.course.CourseRepository;
import io.github.jonarzz.edu.domain.course.Views.CourseView;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class CreateCourseCommandHandler implements CommandHandler<CreateCourseCommand, CourseView> {

    CourseRepository courseRepository;
    CourseConfiguration courseConfig;
    ProfessorRepository professorRepository;
    ProfessorConfiguration professorConfig;

    @Override
    public Result<CourseView> handle(CreateCourseCommand command) {
        var facultyId = command.facultyId();
        var existingCourses = courseRepository.getFacultyCourses(facultyId);
        var courseFactory = new CourseFactory(courseConfig);
        var coursePreparationResult = courseFactory.prepareCourse(
                existingCourses,
                command.courseName(),
                command.fieldsOfStudy()
        );
        if (coursePreparationResult.isNotOk()) {
            return coursePreparationResult.mapFailure();
        }
        var professorId = command.professorId();
        var creationResult = professorRepository.getBy(facultyId, professorId)
                                                .toDomainObject(professorConfig)
                                                .createCourse(coursePreparationResult.getSubject());
        if (creationResult.isOk()) {
            courseRepository.saveNew(facultyId, creationResult.getSubject());
        }
        return creationResult;
    }
}
