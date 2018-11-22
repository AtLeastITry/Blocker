package assignment.models;

public class SpectateResponse extends Response {
    public Game game;

    public SpectateResponse(boolean success, Game game) {
        super(success);
        this.game = game;
    }
}
