package assignment.domain.commands.game.makeMove;

import java.util.concurrent.CompletableFuture;

import assignment.domain.commands.Command;
import assignment.domain.events.game.makeMove.MakeMoveEvent;
import assignment.domain.events.game.makeMove.MakeMoveEventPacket;
import assignment.infrastructure.commands.ICommand;
import assignment.socket.Context;

public class MakeMoveCommand extends Command<Context> implements ICommand<MakeMoveRequest> {
    public MakeMoveCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(MakeMoveRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        this.getContext().games.stream().filter(game -> game.name.equals(request.gameName))
        .forEach(game -> {
            this.getContext().players.stream()
                                  .filter(player -> player.getSessionId().equals(request.currentUser.getId()))
                                  .forEach(player -> {
                                      player.setNextMove(request.move);
                                      player.makeMove(game);
                                  });

            new MakeMoveEvent(this.getContext()).fire(new MakeMoveEventPacket(game));
        });

        return promise;
    }

}