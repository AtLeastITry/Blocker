package assignment.domain.commands.game.spectate;

import java.util.concurrent.CompletableFuture;

import assignment.domain.commands.Command;
import assignment.domain.events.game.spectate.SpectateEvent;
import assignment.domain.events.game.spectate.SpectateEventPacket;
import assignment.infrastructure.commands.ICommand;
import assignment.socket.Context;

public class SpectateCommand extends Command<Context> implements ICommand<SpectateCommandRequest> {
    public SpectateCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(SpectateCommandRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        this.getContext().games.stream().filter(game -> game.name.equals(request.gameName))
        .forEach(game -> {
            game.addSpectator(request.currentUser.getId());
            new SpectateEvent(this.getContext()).fire(new SpectateEventPacket(game, request.currentUser));
        });

        return promise;
    }

}