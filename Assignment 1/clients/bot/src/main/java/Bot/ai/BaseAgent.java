package Bot.ai;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import Bot.util.*;
import Bot.models.*;
import Bot.models.Requests.*;

public abstract class BaseAgent implements IAgent {
    protected final Client _client = new Client(Config.SERVER);

    public BaseAgent() throws URISyntaxException {}

    protected Player _player;
    protected Game _game;
    protected ArrayList<String> _avaliableGameNames;

    protected Set<InfluenceCard> getPlayerCards() {
        return this._game.getPlayerCards(this._player.id);
    }

    protected Boolean playerLost() {
        if (_game == null || _game.players == null) {
            return false;
        }

        UserPlayer player = _game.getPlayer(_player.id);
        return !player.canMove;
    }

    protected Boolean playerWon() {
        return !playerLost() && _game.finished;
    }

    public void run() {
        _client.open(this);
    }

    public abstract Move calculateNextMove();

    public void makeMove() throws InterruptedException {
        if (playerLost() || playerWon()) {
            _client.close();
        }

        if (_game.playerTurn == _player.id) {
            Thread.sleep(1000);

            Move move = this.calculateNextMove();

            _client.Send(new Message<MoveRequest>(MessageType.PLAYER_MOVE, _player.username, new MoveRequest(_game.name, move)));
        }
    }

    public void updateGame(Game game) {
        _game = game;
    }

    public void updatePlayer(Player player) {
        _player = player;
    }

    public void updateAvaliableGames(ArrayList<String> games) {
        _avaliableGameNames = games;
    }

    public void updateAvaliableGames(String newGame) {
        _avaliableGameNames.add(newGame);
    }

    public void initialise() {
        if (_avaliableGameNames.size() > 0) {
            _client.Send(new Message<Request>(MessageType.JOIN, "client", new Request(_avaliableGameNames.get(0))));
            return;
        }
        
        _client.Send(new Message<String>(MessageType.HOST, "client", ""));
        return;
    }

    public abstract void moveResult(boolean moveAllowed, Move move);
}