package assignment.domain.commands.game.leave;

import javax.websocket.Session;

import assignment.infrastructure.commands.ICommandRequest;

public class LeaveCommandRequest implements ICommandRequest{
    public LeaveCommandRequest(Session currentUser, String gameName) {
        this.gameName = gameName;
        this.currentUser = currentUser;
    }
    
    public Session currentUser;
    public String gameName;
}