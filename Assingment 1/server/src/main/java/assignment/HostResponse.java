package assignment;

public class HostResponse {
    public boolean success;
    public GameState game;
    public int playerId;

    public HostResponse(boolean success, GameState game, int playerId) {
        this.success = success;
        this.game = game;
        this.playerId = playerId;
    }
}
