package assignment.infrastructure.commands;

import java.util.concurrent.CompletableFuture;

public interface ICommand<TRequest extends ICommandRequest> {
    CompletableFuture<Boolean> executeAsync(TRequest request);
}