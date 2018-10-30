package assignment.models;

import assignment.GameState;

public class PlayerMoveResponse extends Response {
    public GameState game;

    public PlayerMoveResponse(boolean success, GameState game) {
        super(success);
        this.game = game;
    }
}
