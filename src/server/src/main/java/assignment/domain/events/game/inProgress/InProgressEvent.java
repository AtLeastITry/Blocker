package assignment.domain.events.game.inProgress;

import com.google.gson.Gson;

import assignment.domain.commands.communication.broadcast.BroadcastCommand;
import assignment.domain.commands.communication.broadcast.BroadcastRequest;
import assignment.domain.events.Event;
import assignment.infrastructure.events.IEvent;
import assignment.models.Game;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.responses.NewGameInProgressResponse;
import assignment.socket.Context;

public class InProgressEvent extends Event<Context> implements IEvent<InProgressEventPacket> {
    private final BroadcastCommand broadcastCommand = new BroadcastCommand(this.getContext());

    public InProgressEvent(Context context) {
        super(context);
    }

    @Override
    public void fire(InProgressEventPacket packet) {
        Message message = new Message();
        message.type = MessageType.NEW_GAME_IN_PROGRESS;
        message.sender = "Server";
        message.data = new Gson().toJson(new NewGameInProgressResponse(true, new Game(packet.game.name, packet.game.getNumberOfPlayers())));
        broadcastCommand.executeAsync(new BroadcastRequest(message));
	}
}