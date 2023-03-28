package io.github.jonarzz.edu.domain.faculty;

import io.github.jonarzz.edu.api.CommandHandler;
import io.github.jonarzz.edu.api.result.Result;
import io.github.jonarzz.edu.domain.student.StudentRepository;
import io.github.jonarzz.edu.domain.student.StudentView;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class EnrollStudentCommandHandler implements CommandHandler<EnrollStudentCommand, StudentView> {

    FacultyConfiguration facultyConfiguration;
    FacultyRepository facultyRepository;
    StudentRepository studentRepository;

    @Override
    public Result<StudentView> handle(EnrollStudentCommand command) {
        var studentsView = facultyRepository.getFacultyStudents(command.facultyId());
        var facultyStudents = studentsView.toDomainObject(facultyConfiguration);
        var result = facultyStudents.enroll(command.candidate());
        if (result.isOk()) {
            studentRepository.saveNew(studentsView.facultyId(), result.getSubject());
        }
        return result;
    }
}
