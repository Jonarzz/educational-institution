package io.github.jonarzz.edu.api;

import io.vavr.control.*;

public interface SyncCommandDispatcher<I extends Injector> {

    <C extends Command<I>> Either<FailedResult, SuccessfulResult> handle(C command);

}
