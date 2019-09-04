package Bot.models.Responses;

import Bot.models.Game;

public class LeaveResponse extends Response {
    public Game game;
    public boolean hasLeft;
    public LeaveResponse() {
        super();
    }
}