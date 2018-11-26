package assignment.domain.commands.game.spectate;

import javax.websocket.Session;

import assignment.infrastructure.commands.ICommandRequest;

public class SpectateCommandRequest implements ICommandRequest{
    public SpectateCommandRequest(Session currentUser, String gameName) {
        this.gameName = gameName;
        this.currentUser = currentUser;
    }
    
    public Session currentUser;
    public String gameName;
}