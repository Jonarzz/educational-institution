package io.github.jonarzz.edu.domain.faculty;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class CreateFacultyCommandHandler implements CommandHandler<CreateFacultyCommand, FacultyView> {

    FacultyRepository facultyRepository;

    @Override
    public Result<FacultyView> handle(CreateFacultyCommand command) {
        var institutionId = command.educationalInstitutionId();
        var faculties = facultyRepository.getByEducationalInstitutionId(institutionId)
                                         .toDomainObject();
        var result = faculties.createFaculty(command.name(),
                                             command.fieldsOfStudy(),
                                             command.maxProfessorVacancies(),
                                             command.maxStudentVacancies());
        if (result.isOk()) {
            facultyRepository.create(institutionId, result.getSubject());
        }
        return result;
    }
}
