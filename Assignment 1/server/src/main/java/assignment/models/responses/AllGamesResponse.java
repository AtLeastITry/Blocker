package assignment.models.responses;

import java.util.List;

public class AllGamesResponse extends Response {
    public List<String> gameNames;

    public AllGamesResponse(boolean success, List<String> gameNames) {
        super(success);
        this.gameNames = gameNames;
    }
}
