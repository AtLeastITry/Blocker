package assignment.domain.commands.game.inProgress;

import javax.websocket.Session;

import assignment.infrastructure.commands.ICommandRequest;

public class InProgressRequest implements ICommandRequest {
    public Session currentUser;

    public InProgressRequest(Session currentUser) {
        this.currentUser = currentUser;
    }
}