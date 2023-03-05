package io.github.jonarzz.edu.domain.professor;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.api.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
class ProfessorResignationCommandHandler implements CommandHandler<ProfessorResignationCommand, ProfessorView> {

    ProfessorRepository professorRepository;
    ProfessorResignationListener professorResignationListener;

    @Override
    public Result<ProfessorView> handle(ProfessorResignationCommand command) {
        var professor = professorRepository.getById(command.professorId())
                                           .toDomainObject(professorResignationListener);
        var result = professor.resign(command.resignationReason());
        if (result.isOk()) {
            professorRepository.update(result.getSubject());
        }
        return result;
    }
}
