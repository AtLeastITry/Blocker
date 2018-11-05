package assignment.models;

import assignment.Move;

public class MoveResponse {
    public Move move;
    public Boolean allowed;

    public MoveResponse(Move move, Boolean allowed) {
        this.move = move;
        this.allowed = allowed;
    }
}