package assignment.models.requests;

import assignment.models.Move;

public class CheckMoveRequest extends Request {
    public Move move;
    public int playerId;

    public CheckMoveRequest(String gameName, Move move, int playerId) {
        super(gameName);
        this.move = move;
        this.playerId = playerId;
    }
}
