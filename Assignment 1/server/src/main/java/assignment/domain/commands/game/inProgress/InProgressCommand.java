package assignment.domain.commands.game.inProgress;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import assignment.domain.commands.Command;
import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.infrastructure.commands.ICommand;
import assignment.models.Game;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.GamesInProgressResponse;
import assignment.socket.Context;

public class InProgressCommand extends Command<Context> implements ICommand<InProgressRequest> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public InProgressCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(InProgressRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        List<Game> gamesInprogress = this.getContext().games.stream()
                                               .filter(game -> game.getInPogress())
                                               .map(game -> new Game(game.name, game.getNumberOfPlayers()))
                                               .collect(Collectors.toList());

        Message message = new Message();
        message.type = MessageType.GAMES_IN_PROGRESS;
        message.sender = "Server";
        message.data = new Gson().toJson(new GamesInProgressResponse(true, gamesInprogress));

        broadcastCommand.executeAsync(new BroadcastRequest(message), request.currentUser);
        return promise;
	}

}