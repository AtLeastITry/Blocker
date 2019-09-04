package assignment.domain.commands.game.all;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import assignment.domain.commands.Command;
import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.infrastructure.commands.ICommand;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.AllGamesResponse;
import assignment.socket.Context;

public class AllCommand extends Command<Context> implements ICommand<AllRequest> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public AllCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(AllRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();
        List<String> gameNames = this.getContext().games.stream()
                                           .filter(game -> !game.getInPogress() && game.getNumberOfPlayers() < 5)
                                           .map(game -> game.name).collect(Collectors.toList());
        
        Message message = new Message();
        message.type = MessageType.ALL_GAMES;
        message.sender = "Server";
        message.data = new Gson().toJson(new AllGamesResponse(true, gameNames));

        broadcastCommand.executeAsync(new BroadcastRequest(message));
        return promise;
	}

}