package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.api.*;

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
                                             command.fieldsOfStudy());
        if (result.isOk()) {
            var faculty = result.getSubject()
                                .orElseThrow(() -> new IllegalStateException(
                                        "No subject returned after creating a faculty"));
            facultyRepository.save(institutionId, faculty);
        }
        return result;
    }
}