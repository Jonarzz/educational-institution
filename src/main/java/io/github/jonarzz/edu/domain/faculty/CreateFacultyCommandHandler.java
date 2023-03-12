package io.github.jonarzz.edu.domain.faculty;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.faculty.Views.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class CreateFacultyCommandHandler implements CommandHandler<CreateFacultyCommand, NewFacultyView> {

    FacultyRepository facultyRepository;

    @Override
    public Result<NewFacultyView> handle(CreateFacultyCommand command) {
        var institutionId = command.educationalInstitutionId();
        var faculties = new Faculties(
                facultyRepository.getAllFacultyNames(institutionId)
        );
        var result = faculties.createFaculty(command.name(),
                                             command.fieldsOfStudy(),
                                             command.maxProfessorVacancies(),
                                             command.maxStudentVacancies());
        if (result.isOk()) {
            facultyRepository.saveNew(institutionId, result.getSubject());
        }
        return result;
    }
}
