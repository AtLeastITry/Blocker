package assignment.domain.events.game.host;

import com.google.gson.Gson;

import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.domain.events.Event;
import assignment.infrastructure.events.IEvent;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.HostResponse;
import assignment.models.responses.NewGameResponse;
import assignment.socket.Context;

public class HostEvent extends Event<Context> implements IEvent<HostEventPacket>{
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public HostEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(HostEventPacket packet) {
        broadcastToCurrentUser(packet);
        broadcastToAllUsers(packet);
    }
    
    private void broadcastToCurrentUser(HostEventPacket packet) {
        Message reply = new Message();
        reply.type = MessageType.HOST;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new HostResponse(true, packet.game, packet.playerId));
        broadcastCommand.executeAsync(new BroadcastRequest(reply), packet.currentUser);
    }

    private void broadcastToAllUsers(HostEventPacket packet) {
        Message newGameResponse = new Message();
        newGameResponse.type = MessageType.NEW_GAME;
        newGameResponse.sender = "Server";
        newGameResponse.data = new Gson().toJson(new NewGameResponse(true, packet.game.name));
        broadcastCommand.executeAsync(new BroadcastRequest(newGameResponse));
    }
}