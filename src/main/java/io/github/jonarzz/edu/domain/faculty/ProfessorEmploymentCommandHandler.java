package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.result.*;
import io.github.jonarzz.edu.domain.professor.*;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ProfessorEmploymentCommandHandler implements CommandHandler<EmployProfessorCommand, ProfessorView> {

    FacultyConfiguration facultyConfiguration;
    FacultyRepository facultyRepository;
    ProfessorRepository professorRepository;

    @Override
    public Result<ProfessorView> handle(EmployProfessorCommand command) {
        var facultyName = command.facultyName();
        var facultiesView = facultyRepository.getByEducationalInstitutionId(command.educationalInstitutionId());
        return facultiesView.faculties()
                            .stream()
                            .filter(faculty -> faculty.name()
                                                      .equals(facultyName))
                            .findFirst()
                            .map(facultyView -> {
                                var faculty = facultyView.toDomainObject(facultyConfiguration);
                                var result = faculty.employ(command.candidate());
                                if (result.isOk()) {
                                    var professor = result.getSubject()
                                                          .orElseThrow(() -> new IllegalStateException(
                                                                  "No subject returned after employing a professor"));
                                    // TODO save faculty, not a professor
                                    professorRepository.save(facultyView.id(), professor);
                                }
                                return result;
                            })
                            .orElseGet(() -> new NotFound<>("faculty", "name", facultyName));
    }
}
