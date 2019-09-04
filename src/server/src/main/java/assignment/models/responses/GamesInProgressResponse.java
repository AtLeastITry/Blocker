package assignment.models.responses;

import java.util.List;

import assignment.models.Game;

public class GamesInProgressResponse extends Response {
    public List<Game> games;

    public GamesInProgressResponse(boolean success, List<Game> games) {
        super(success);
        this.games = games;
    }
}
