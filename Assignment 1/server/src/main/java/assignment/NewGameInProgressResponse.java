package assignment;

import assignment.models.Game;

public class NewGameInProgressResponse {
    public boolean success;
    public Game game;

    public NewGameInProgressResponse(boolean success, Game game) {
        this.success = success;
        this.game = game;
    }
}
