package assignment.models;

public class LeaveRequest extends Request {
    public int playerId;

    public LeaveRequest(String gameName, int playerId) {
        super(gameName);

        this.playerId = playerId;
    }
}
