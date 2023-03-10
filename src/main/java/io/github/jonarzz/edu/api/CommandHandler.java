package io.github.jonarzz.edu.api;

import io.github.jonarzz.edu.api.result.*;

public interface CommandHandler<C extends Command, R> {

    Result<R> handle(C command);

}
