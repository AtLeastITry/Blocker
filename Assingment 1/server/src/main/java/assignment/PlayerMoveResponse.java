package assignment;

public class PlayerMoveResponse {
    public boolean success;
    public GameState game;

    public PlayerMoveResponse(boolean success, GameState game) {
        this.success = success;
        this.game = game;
    }
}
