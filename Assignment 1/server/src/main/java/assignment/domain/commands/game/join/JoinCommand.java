package assignment.domain.commands.game.join;

import java.util.concurrent.CompletableFuture;

import assignment.domain.commands.Command;
import assignment.domain.commands.game.all.AllCommand;
import assignment.domain.commands.game.all.AllRequest;
import assignment.domain.events.game.join.JoinEvent;
import assignment.domain.events.game.join.JoinEventPacket;
import assignment.infrastructure.commands.ICommand;
import assignment.models.Player;
import assignment.socket.Context;

public class JoinCommand extends Command<Context> implements ICommand<JoinRequest> {
    public JoinCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(JoinRequest request) {
		CompletableFuture<Boolean> promise = new CompletableFuture<>();
        Player player = new Player(request.sessionId);
        this.getContext().games.stream().filter(game -> game.name.equals(request.gameName))
        .forEach(game -> {
            game.addPlayer(request.sessionId, player);
            this.getContext().players.add(player);
            promise.complete(true);
                        
            new JoinEvent(this.getContext()).fire(new JoinEventPacket(game));
        });

        return promise;
	}

}