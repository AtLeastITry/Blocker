package assignment.models;

import assignment.GameState;

public class LeaveResponse extends Response {
    public GameState game;
    public boolean hasLeft;
    public LeaveResponse(boolean success, GameState game, boolean hasLeft) {
        super(success);

        this.game = game;
        this.hasLeft = hasLeft;
    }
}
