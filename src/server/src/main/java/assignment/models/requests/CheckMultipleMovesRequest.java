package assignment.models.requests;

import java.util.ArrayList;

import assignment.models.Move;

public class CheckMultipleMovesRequest extends Request {
    public ArrayList<Move> moves;
    public int playerId;

    public CheckMultipleMovesRequest(String gameName, ArrayList<Move> moves, int playerId) {
        super(gameName);
        
        this.moves = moves;
        this.playerId = playerId;
    }
}