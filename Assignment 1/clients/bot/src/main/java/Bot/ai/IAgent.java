package Bot.ai;

import java.util.ArrayList;

import Bot.models.Game;
import Bot.models.Move;
import Bot.models.Player;

public interface IAgent {
    public void run();
    public void updateGame(Game game);
    public void updatePlayer(Player player);
    public void updateAvaliableGames(ArrayList<String> games);
    public void updateAvaliableGames(String newGame);
    public void moveResult(boolean moveAllowed, Move move);
    public void makeMove() throws InterruptedException;
    public void initialise();
}