package assignment.domain.events.game.leave;


import java.util.ArrayList;
import java.util.List;
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
import assignment.models.responses.LeaveResponse;
import assignment.socket.Context;

public class LeaveEvent extends Event<Context> implements IEvent<LeaveEventPacket> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public LeaveEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(LeaveEventPacket packet) {
        Message message = new Message();
        message.type = MessageType.LEAVE;
        message.sender = "Server";
        message.data = new Gson().toJson(new LeaveResponse(true, packet.game, false));

        if (packet.game.getNumberOfPlayers() > 0) {
            List<Session> users = this.getContext().users.stream()
            .filter(user -> packet.game.checkPlayerInGame(user.getId()))
            .collect(Collectors.toList());

            broadcastCommand.executeAsync(new BroadcastRequest(message), new ArrayList<Session>(users));
        }

        message.data = new Gson().toJson(new LeaveResponse(true, packet.game, true));
        broadcastCommand.executeAsync(new BroadcastRequest(message), packet.currentUser);

        new AllCommand(this.getContext()).executeAsync(new AllRequest());
	}
}