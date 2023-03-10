package io.github.jonarzz.edu.domain.faculty;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.professor.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class EmployProfessorCommandHandler implements CommandHandler<EmployProfessorCommand, ProfessorView> {

    FacultyConfiguration facultyConfiguration;
    FacultyRepository facultyRepository;
    ProfessorRepository professorRepository;

    @Override
    public Result<ProfessorView> handle(EmployProfessorCommand command) {
        var institutionId = command.educationalInstitutionId();
        var facultyName = command.facultyName();
        var facultyView = facultyRepository.getEducationalInstitutionFaculty(institutionId, facultyName);
        var facultyProfessors = facultyView.professorsDomainObject(facultyConfiguration);
        var result = facultyProfessors.employ(command.candidate());
        if (result.isOk()) {
            professorRepository.create(facultyView.id(), result.getSubject());
        }
        return result;
    }
}
