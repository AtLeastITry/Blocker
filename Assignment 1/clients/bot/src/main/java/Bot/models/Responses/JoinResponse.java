package Bot.models.Responses;

import Bot.models.Game;

public class JoinResponse extends Response {
    public Game game;
    public int playerId;

    public JoinResponse() {
        super();
    }
}