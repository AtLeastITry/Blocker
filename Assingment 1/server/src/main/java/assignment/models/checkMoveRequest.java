package assignment.models;

import assignment.Move;

public class checkMoveRequest extends Request {
    public Move move;
    public int playerId;

    public checkMoveRequest(String gameName, Move move, int playerId) {
        super(gameName);
        this.move = move;
        this.playerId = playerId;
    }
}
