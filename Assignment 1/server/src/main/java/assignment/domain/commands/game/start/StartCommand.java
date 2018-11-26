package assignment.domain.commands.game.start;

import java.util.concurrent.CompletableFuture;

import assignment.domain.commands.Command;
import assignment.domain.events.game.inProgress.InProgressEvent;
import assignment.domain.events.game.inProgress.InProgressEventPacket;
import assignment.domain.events.game.start.StartEvent;
import assignment.domain.events.game.start.StartEventPacket;
import assignment.infrastructure.commands.ICommand;
import assignment.socket.Context;

public class StartCommand extends Command<Context> implements ICommand<StartRequest> {
    public StartCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(StartRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        this.getContext().games.stream().filter(game -> game.name.equals(request.gameName))
        .forEach(game -> {
            // if host
            if (game.getPlayerId(request.sessionId) == 1) {
                game.start();
                new StartEvent(this.getContext()).fire(new StartEventPacket(game));
                new InProgressEvent(this.getContext()).fire(new InProgressEventPacket(game));
            }
        });

        return promise;
    }

}