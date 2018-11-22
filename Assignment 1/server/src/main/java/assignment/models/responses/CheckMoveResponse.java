package assignment.models.responses;

import assignment.models.Move;

public class CheckMoveResponse extends Response {
    public boolean moveAllowed;
    public Move move;
    public CheckMoveResponse(boolean success, boolean moveAllowed, Move move) {
        super(success);

        this.moveAllowed = moveAllowed;
        this.move = move;
    }
}
