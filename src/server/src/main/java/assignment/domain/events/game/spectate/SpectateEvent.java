package assignment.domain.events.game.spectate;

import com.google.gson.Gson;

import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.domain.events.Event;
import assignment.infrastructure.events.IEvent;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.SpectateResponse;
import assignment.socket.Context;

public class SpectateEvent extends Event<Context> implements IEvent<SpectateEventPacket> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public SpectateEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(SpectateEventPacket packet) {
        Message message = new Message();
        message.type = MessageType.SPECTATE_GAME;
        message.sender = "Server";
        message.data = new Gson().toJson(new SpectateResponse(true, packet.game));

        broadcastCommand.executeAsync(new BroadcastRequest(message), packet.currentUser);
	}
}