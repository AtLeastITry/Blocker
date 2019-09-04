package assignment.models.responses;

import assignment.models.Move;

public class MoveResponse {
    public Move move;
    public Boolean allowed;

    public MoveResponse(Move move, Boolean allowed) {
        this.move = move;
        this.allowed = allowed;
    }
}