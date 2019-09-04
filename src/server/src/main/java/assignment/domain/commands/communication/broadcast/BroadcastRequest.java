package assignment.domain.commands.communication.broadcast;

import assignment.infrastructure.commands.ICommandRequest;
import assignment.models.Message;

public class BroadcastRequest implements ICommandRequest {
    public Message message;

    public BroadcastRequest(Message message) {
        this.message = message;
    }
}