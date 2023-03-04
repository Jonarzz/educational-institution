package io.github.jonarzz.edu.api;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.domain.*;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SyncCommandDispatcher {

    DomainInjector injector;

    public Result<Object> handle(Command command) {
        return command.getHandler(injector)
                      .handle(command);
    }
}
