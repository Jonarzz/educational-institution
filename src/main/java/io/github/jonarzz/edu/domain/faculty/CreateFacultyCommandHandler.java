package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.common.result.RejectionReason.*;
import static io.vavr.control.Either.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;

import io.vavr.control.*;
import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.result.*;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class CreateFacultyCommandHandler implements CommandHandler<CreateFacultyCommand, FacultyInjector> {

    FacultyRepository facultyRepository;

    @Override
    public Either<FailedResult, SuccessfulResult> handle(CreateFacultyCommand command) {
        return facultyRepository.findByEducationalInstitutionId(command.educationalInstitutionId())
                                .map(Faculties::fromView)
                                .map(faculties -> {
                                    var result = faculties.createFaculty(command.name(),
                                                                         command.fieldsOfStudy());
                                    if (result.isRight()) {
                                        facultyRepository.save(faculties.toView());
                                    }
                                    //noinspection UnnecessaryLocalVariable (not strictly matching generics)
                                    Either<FailedResult, SuccessfulResult> mapped = result.bimap(
                                            identity(),
                                            Created::new
                                    );
                                    return mapped;
                                })
                                .orElseGet(() -> left(ENTITY_NOT_FOUND));
    }
}
