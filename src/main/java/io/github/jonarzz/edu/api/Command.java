package io.github.jonarzz.edu.api;

import io.github.jonarzz.edu.domain.*;

public interface Command {

    <C extends Command> CommandHandler<C, Object> getHandler(DomainInjector injector);

}
