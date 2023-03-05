package io.github.jonarzz.edu.api;

import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.domain.*;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SyncCommandDispatcher {

    DomainInjector injector;

    public Result<Object> handle(Command command) {
        return command.getHandler(injector)
                      .handle(command);
    }
}
