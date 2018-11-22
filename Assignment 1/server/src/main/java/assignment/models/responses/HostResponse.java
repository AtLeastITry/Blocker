package assignment.models.responses;

import assignment.models.GameState;

public class HostResponse extends Response {
    public GameState game;
    public int playerId;

    public HostResponse(boolean success, GameState game, int playerId) {
        super(success);
        this.success = success;
        this.game = game;
        this.playerId = playerId;
    }
}
