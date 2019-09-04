package assignment.models.responses;

import assignment.models.Game;

public class NewGameInProgressResponse extends Response {
    public Game game;

    public NewGameInProgressResponse(boolean success, Game game) {
        super(success);
        this.game = game;
    }
}
