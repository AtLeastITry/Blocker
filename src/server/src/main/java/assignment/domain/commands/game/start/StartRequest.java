package assignment.domain.commands.game.start;

import assignment.infrastructure.commands.ICommandRequest;

public class StartRequest implements ICommandRequest{
    public StartRequest(String sessionId, String gameName) {
        this.gameName = gameName;
        this.sessionId = sessionId;
    }
    
    public String sessionId;
    public String gameName;
}