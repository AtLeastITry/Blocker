package assignment.domain.events.game.makeMove;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.Session;

import com.google.gson.Gson;

import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.domain.events.Event;
import assignment.infrastructure.events.IEvent;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.PlayerMoveResponse;
import assignment.socket.Context;

public class MakeMoveEvent extends Event<Context> implements IEvent<MakeMoveEventPacket> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public MakeMoveEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(MakeMoveEventPacket packet) {
        Message message = new Message();
        message.type = MessageType.PLAYER_MOVE;
        message.sender = "Server";
        message.data = new Gson().toJson(new PlayerMoveResponse(true, packet.game));

		List<Session> users = this.getContext().users.stream()
        .filter(user -> packet.game.checkPlayerInGame(user.getId()))
        .collect(Collectors.toList());

        broadcastCommand.executeAsync(new BroadcastRequest(message), new ArrayList<Session>(users));
	}
}