package io.github.jonarzz.edu.api;

public interface CommandHandler<C extends Command, R> {

    Result<R> handle(C command);

}
