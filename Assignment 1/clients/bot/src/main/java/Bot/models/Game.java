package Bot.models;
import java.util.*;

public class Game {
    public int[][] board;
    public Map<Integer, Set<InfluenceCard>> influenceCards;
    public ArrayList<UserPlayer> players;
    public Boolean inProgress;
    public Boolean finished;
    public String name;
    public int playerTurn;

    public int getNumPlayers() {
        return players.size();
    };

    public Set<InfluenceCard> getPlayerCards(int playerId) {
        return influenceCards.get(playerId);
    }

    public UserPlayer getPlayer(int playerId) {
        UserPlayer player = null;

        for (int i = 0; i < players.size(); i++) {
            UserPlayer temp = players.get(i);
            if (temp.playerId == playerId) {
                player = temp;
                break;
            }
        }

        return player;
    }
}