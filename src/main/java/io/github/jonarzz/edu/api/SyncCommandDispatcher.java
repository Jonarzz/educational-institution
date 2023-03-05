package io.github.jonarzz.edu.api;

import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.domain.*;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SyncCommandDispatcher {

    DomainInjector injector;

    // TODO implement and inject
    EventEmitter eventEmitter = new EventEmitter();

    public void handle(Command command) {
        var result = command.getHandler(injector)
                            .handle(command);
        result.toEvent()
              .ifPresent(eventEmitter::emit);
    }
}
