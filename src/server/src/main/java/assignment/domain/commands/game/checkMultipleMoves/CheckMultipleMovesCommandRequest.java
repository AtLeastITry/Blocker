package assignment.domain.commands.game.checkMultipleMoves;

import java.util.ArrayList;

import javax.websocket.Session;

import assignment.infrastructure.commands.ICommandRequest;
import assignment.models.Move;

public class CheckMultipleMovesCommandRequest implements ICommandRequest{
    public CheckMultipleMovesCommandRequest(Session currentUser, String gameName, ArrayList<Move> moves) {
        this.currentUser = currentUser;
        this.gameName = gameName;
        this.moves = moves;
    }

    public Session currentUser;
    public String gameName;
    public ArrayList<Move> moves;
}