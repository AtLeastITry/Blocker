package assignment;

public class UserPlayer {
    public int playerId;
    public String sessionId;
    public boolean canMove;

    public UserPlayer(int playerId, String sessionId) {
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.canMove = true;
    }
}
