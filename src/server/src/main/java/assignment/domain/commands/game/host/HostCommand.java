package assignment.domain.commands.game.host;

import java.util.concurrent.CompletableFuture;

import assignment.domain.commands.Command;
import assignment.domain.events.game.host.HostEvent;
import assignment.domain.events.game.host.HostEventPacket;
import assignment.infrastructure.commands.ICommand;
import assignment.models.GameState;
import assignment.models.Player;
import assignment.socket.Context;

public class HostCommand extends Command<Context> implements ICommand<HostRequest> {
    public HostCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(HostRequest request) {
		CompletableFuture<Boolean> promise = new CompletableFuture<>();

        try {
            Player player = new Player(request.currentUser.getId());
            GameState game = new GameState("Game " + (this.getContext().games.size() + 1));

            game.addPlayer(request.currentUser.getId(), player);
            this.getContext().players.add(player);
            this.getContext().games.add(game);

            promise.complete(true);

            new HostEvent(this.getContext()).fire(new HostEventPacket(game, player.getMyPlayerId(), request.currentUser));
        }
        catch(Exception e) {
            promise.completeExceptionally(e);
        }

        return promise;
	}

}