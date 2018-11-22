package assignment;

import java.util.List;

import assignment.models.Game;

public class GamesInProgressResponse {
    public boolean success;
    public List<Game> games;

    public GamesInProgressResponse(boolean success, List<Game> games) {
        this.success = success;
        this.games = games;
    }
}
