package assignment.models.responses;

import assignment.models.GameState;

public class PlayerMoveResponse extends Response {
    public GameState game;

    public PlayerMoveResponse(boolean success, GameState game) {
        super(success);
        this.game = game;
    }
}
