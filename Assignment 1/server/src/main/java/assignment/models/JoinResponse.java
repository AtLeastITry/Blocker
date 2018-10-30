package assignment.models;

import assignment.GameState;

public class JoinResponse extends Response{
    public GameState game;
    public int playerId;

    public JoinResponse(boolean success, GameState game, int playerId) {
        super(success);
        this.success = success;
        this.game = game;
        this.playerId = playerId;
    }
}
