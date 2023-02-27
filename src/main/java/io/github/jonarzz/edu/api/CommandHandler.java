package io.github.jonarzz.edu.api;

import io.vavr.control.*;

public interface CommandHandler<C extends Command<I>, I extends Injector> {

    Either<FailedResult, SuccessfulResult> handle(C command);

}
