package io.github.jonarzz.edu.domain.faculty;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.student.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class EnrollStudentCommandHandler implements CommandHandler<EnrollStudentCommand, StudentView> {

    FacultyConfiguration facultyConfiguration;
    FacultyRepository facultyRepository;
    StudentRepository studentRepository;

    @Override
    public Result<StudentView> handle(EnrollStudentCommand command) {
        var institutionId = command.educationalInstitutionId();
        var facultyName = command.facultyName();
        var facultyView = facultyRepository.getEducationalInstitutionFaculty(institutionId, facultyName);
        var facultyStudents = facultyView.studentsDomainObject(facultyConfiguration);
        var result = facultyStudents.enroll(command.candidate());
        if (result.isOk()) {
            studentRepository.create(facultyView.id(), result.getSubject());
        }
        return result;
    }
}
