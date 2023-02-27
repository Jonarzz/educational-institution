package io.github.jonarzz.edu.api;

public interface Command<I extends Injector> {

    <C extends Command<I>> CommandHandler<C, I> getHandler(I injector);

}
