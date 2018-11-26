package assignment.domain.commands.game.host;

import javax.websocket.Session;

import assignment.infrastructure.commands.ICommandRequest;

public class HostRequest implements ICommandRequest {
    public HostRequest(Session currentUser) {
        this.currentUser = currentUser;
    }
    
    public Session currentUser;
}