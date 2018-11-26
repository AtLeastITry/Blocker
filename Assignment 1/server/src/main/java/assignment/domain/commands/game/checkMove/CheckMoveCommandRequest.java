package assignment.domain.commands.game.checkMove;

import javax.websocket.Session;

import assignment.infrastructure.commands.ICommandRequest;
import assignment.models.Move;

public class CheckMoveCommandRequest implements ICommandRequest{
    public CheckMoveCommandRequest(Session currentUser, String gameName, Move move) {
        this.currentUser = currentUser;
        this.gameName = gameName;
        this.move = move;
    }

    public Session currentUser;
    public String gameName;
    public Move move;
}