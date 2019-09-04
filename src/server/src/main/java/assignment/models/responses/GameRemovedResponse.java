package assignment.models.responses;

public class GameRemovedResponse extends Response {
    public String gameName;

    public GameRemovedResponse(boolean success, String gameName) {
        super(success);
        this.success = success;
        this.gameName = gameName;
    }
}
