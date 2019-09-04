package assignment.domain.events.game.start;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.websocket.Session;

import com.google.gson.Gson;

import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.domain.commands.game.all.AllCommand;
import assignment.domain.commands.game.all.AllRequest;
import assignment.domain.events.Event;
import assignment.infrastructure.events.IEvent;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.StartResponse;
import assignment.socket.Context;

public class StartEvent extends Event<Context> implements IEvent<StartEventPacket> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public StartEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(StartEventPacket packet) {
        Message message = new Message();
        message.type = MessageType.START;
        message.sender = "Server";
        message.data = new Gson().toJson(new StartResponse(true, packet.game));

		List<Session> users = this.getContext().users.stream()
        .filter(user -> packet.game.checkPlayerInGame(user.getId()))
        .collect(Collectors.toList());

        broadcastCommand.executeAsync(new BroadcastRequest(message), new ArrayList<Session>(users));

        new AllCommand(this.getContext()).executeAsync(new AllRequest());
	}
}