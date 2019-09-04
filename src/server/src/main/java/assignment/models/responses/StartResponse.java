package assignment.models.responses;

import assignment.models.GameState;

public class StartResponse extends Response {
    public GameState game;
    public StartResponse(boolean success, GameState game) {
        super(success);

        this.game = game;
    }
}
