package assignment;

import java.util.List;

public class AllGamesResponse {
    public boolean success;
    public List<String> gameNames;

    public AllGamesResponse(boolean success, List<String> gameNames) {
        this.success = success;
        this.gameNames = gameNames;
    }
}
