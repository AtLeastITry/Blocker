package assignment.domain.events.game.join;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.domain.commands.game.all.AllCommand;
import assignment.domain.commands.game.all.AllRequest;
import assignment.domain.events.Event;
import assignment.infrastructure.events.IEvent;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.JoinResponse;
import assignment.socket.Context;

public class JoinEvent extends Event<Context> implements IEvent<JoinEventPacket> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public JoinEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(JoinEventPacket packet) {
        Message reply = new Message();
        reply.type = MessageType.JOIN;
        reply.sender = "Server";

		this.getContext().users.stream().forEach(user -> {
            Integer playerId = packet.game.getPlayerId(user.getId());
            if (playerId != null) {
                reply.data = new Gson().toJson(new JoinResponse(true, packet.game, playerId));
                broadcastCommand.executeAsync(new BroadcastRequest(reply), user);
            }
        });

        new AllCommand(this.getContext()).executeAsync(new AllRequest());
	}
}