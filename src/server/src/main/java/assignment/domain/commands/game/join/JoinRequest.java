package assignment.domain.commands.game.join;

import assignment.infrastructure.commands.ICommandRequest;
import assignment.models.GameState;

public class JoinRequest implements ICommandRequest{
    public JoinRequest(String sessionId, String gameName) {
        this.sessionId = sessionId;
        this.gameName = gameName;
    }
    
    public String sessionId;
    public String gameName;
}