package assignment.domain.commands.game.checkMultipleMoves;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import assignment.domain.commands.Command;
import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.infrastructure.commands.ICommand;
import assignment.models.GameState;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.CheckMoveResponse;
import assignment.models.responses.CheckMultipleMovesResponse;
import assignment.models.responses.MoveResponse;
import assignment.services.MoveValidator;
import assignment.socket.Context;

public class CheckMultipleMovesCommand extends Command<Context> implements ICommand<CheckMultipleMovesCommandRequest> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public CheckMultipleMovesCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(CheckMultipleMovesCommandRequest request) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

        Optional<GameState> game = this.getContext().games.stream().filter(g -> g.name.equals(request.gameName)).findFirst();

        if (game.isPresent()) {
            Integer playerId = game.get().getPlayerId(request.currentUser.getId());
            if (playerId == null) {
                promise.completeExceptionally(new Exception("Invalid player"));
            }

            MoveValidator validator = new MoveValidator(game.get());
            List<MoveResponse> moves = request.moves.stream()
                                                    .map(move -> new MoveResponse(move, validator.validate(move, playerId)))
                                                    .collect(Collectors.toList());

            Message message = new Message();
            message.type = MessageType.CHECK_MULTIPLE_MOVES;
            message.sender = "Server";
            message.data = new Gson().toJson(new CheckMultipleMovesResponse(true, moves));

            broadcastCommand.executeAsync(new BroadcastRequest(message), request.currentUser);

            promise.complete(true);
        }
        else {
            promise.complete(false);
        }

        return promise;
    }

}