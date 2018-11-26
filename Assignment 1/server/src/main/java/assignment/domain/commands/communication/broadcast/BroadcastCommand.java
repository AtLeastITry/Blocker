package assignment.domain.commands.communication.broadcast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import assignment.domain.commands.Command;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.infrastructure.commands.ICommand;
import assignment.socket.Context;

public class BroadcastCommand extends Command<Context> implements ICommand<BroadcastRequest> {
    public BroadcastCommand(Context context) {
        super(context);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(BroadcastRequest request) {
        return this.executeAsync(request, this.getContext().users);
    }
    
    public CompletableFuture<Boolean> executeAsync(BroadcastRequest request, ArrayList<Session> users) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();

		users.stream().forEach(user -> {
            try {
                user.getBasicRemote().sendObject(request.message);
            } catch (IOException | EncodeException e) {
                promise.completeExceptionally(e);
            }
        });

        promise.complete(true);

        return promise;
	}

    public CompletableFuture<Boolean> executeAsync(BroadcastRequest request, Session user) {
        CompletableFuture<Boolean> promise = new CompletableFuture<>();
        try {
            user.getBasicRemote().sendObject(request.message);
        } catch (IOException | EncodeException e) {
            promise.completeExceptionally(e);
        }

        promise.complete(true);

        return promise;
	}
}