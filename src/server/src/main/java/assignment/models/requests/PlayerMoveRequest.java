package assignment.models.requests;

import assignment.models.Move;

public class PlayerMoveRequest extends Request {
    public Move move;
    public PlayerMoveRequest(String gameName, Move move) {
        super(gameName);

        this.move = move;
    }
}
