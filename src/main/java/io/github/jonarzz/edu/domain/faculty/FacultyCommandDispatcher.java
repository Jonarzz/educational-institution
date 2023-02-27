package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import io.vavr.control.*;
import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.api.*;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FacultyCommandDispatcher implements SyncCommandDispatcher<FacultyInjector> {

    FacultyInjector injector;

    @Override
    public <C extends Command<FacultyInjector>> Either<FailedResult, SuccessfulResult> handle(C command) {
        return command.getHandler(injector)
                      .handle(command);
    }
}
