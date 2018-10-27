package assignment.models;

import assignment.Move;

public class playerMoveRequest extends Request {
    public Move move;
    public playerMoveRequest(String gameName, Move move) {
        super(gameName);

        this.move = move;
    }
}
