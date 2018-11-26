package assignment.domain.commands.game.checkMove;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import assignment.domain.commands.Command;
import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.infrastructure.commands.ICommand;
import assignment.models.GameState;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.CheckMoveResponse;
import assignment.services.MoveValidator;
import assignment.socket.Context;

public class CheckMoveCommand extends Command<Context> implements ICommand<CheckMoveCommandRequest> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public CheckMoveCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(CheckMoveCommandRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        Optional<GameState> game = this.getContext().games.stream().filter(g -> g.name.equals(request.gameName)).findFirst();

        if (game.isPresent()) {
            Integer playerId = game.get().getPlayerId(request.currentUser.getId());
            if (playerId == null) {
                promise.completeExceptionally(new Exception("Invalid player"));
            }

            MoveValidator validator = new MoveValidator(game.get());
            boolean isAllowed = validator.validate(request.move, playerId);

            Message message = new Message();
            message.type = MessageType.CHECK_MOVE;
            message.sender = "Server";
            message.data = new Gson().toJson(new CheckMoveResponse(true, isAllowed, request.move));

            broadcastCommand.executeAsync(new BroadcastRequest(message), request.currentUser);

            promise.complete(true);
        }
        else {
            promise.complete(false);
        }

        return promise;
    }

}