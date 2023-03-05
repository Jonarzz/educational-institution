package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class CreateFacultyCommandHandler implements CommandHandler<CreateFacultyCommand, FacultyView> {

    FacultyRepository facultyRepository;

    @Override
    public Result<FacultyView> handle(CreateFacultyCommand command) {
        var institutionId = command.educationalInstitutionId();
        var facultiesView = facultyRepository.getByEducationalInstitutionId(institutionId);
        var faculties = Faculties.fromView(facultiesView);
        var result = faculties.createFaculty(command.name(),
                                             command.fieldsOfStudy(),
                                             command.maxProfessorVacancies());
        if (result.isOk()) {
            facultyRepository.create(institutionId, result.getSubject());
        }
        return result;
    }
}
