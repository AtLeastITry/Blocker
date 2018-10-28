package assignment.models;

import assignment.Move;

public class checkMoveResponse extends Response {
    public boolean moveAllowed;
    public Move move;
    public checkMoveResponse(boolean success, boolean moveAllowed, Move move) {
        super(success);

        this.moveAllowed = moveAllowed;
        this.move = move;
    }
}
