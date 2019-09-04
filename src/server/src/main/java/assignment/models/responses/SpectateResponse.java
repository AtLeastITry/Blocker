package assignment.models.responses;

import assignment.models.GameState;

public class SpectateResponse extends Response {
    public GameState game;

    public SpectateResponse(boolean success, GameState game) {
        super(success);
        this.game = game;
    }
}
