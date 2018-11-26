package assignment.domain.commands.game.leave;

import java.util.concurrent.CompletableFuture;

import assignment.domain.commands.Command;
import assignment.domain.events.game.leave.LeaveEvent;
import assignment.domain.events.game.leave.LeaveEventPacket;
import assignment.infrastructure.commands.ICommand;
import assignment.socket.Context;

public class LeaveCommand extends Command<Context> implements ICommand<LeaveCommandRequest> {
    public LeaveCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(LeaveCommandRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        this.getContext().games.stream().filter(game -> game.name.equals(request.gameName))
        .forEach(game -> {
            Integer playerId = game.getPlayerId(request.currentUser.getId());
            if (playerId != null) {
                game.removePlayer(playerId);
                new LeaveEvent(this.getContext()).fire(new LeaveEventPacket(game, request.currentUser));
            }
        });

        this.getContext().games.removeIf(game -> game.getNumberOfPlayers() == 0 && game.name.equals(request.gameName));

        return promise;
    }

}