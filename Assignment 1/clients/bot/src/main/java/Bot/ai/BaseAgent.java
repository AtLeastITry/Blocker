package Bot.ai;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import Bot.util.*;
import Bot.models.*;
import Bot.models.Requests.*;
import Bot.models.Responses.*;

public abstract class BaseAgent implements IAgent {
    protected final Client _client = new Client(Config.SERVER);

    public BaseAgent() throws URISyntaxException {
        _avaliableGameNames = new ArrayList<>();
        _initialised = false;
    }

    protected Player _player;
    protected Game _game;
    protected ArrayList<String> _avaliableGameNames;
    protected Boolean _initialised;

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
            Move move = this.calculateNextMove();

            Thread.sleep(1000);

            _client.Send(new Message<MoveRequest>(MessageType.PLAYER_MOVE, _player.username(), new MoveRequest(_game.name, move)));
        }
    }

    public void initialise() {
        if (_avaliableGameNames.size() > 0) {
            _client.Send(new Message<Request>(MessageType.JOIN, "client", new Request(_avaliableGameNames.get(0))));
            return;
        }
        
        _client.Send(new Message<String>(MessageType.HOST, "client", ""));
        return;
    }

    public void allGamesAction(AllGamesResponse response) {
        _avaliableGameNames = response.gameNames;

        if (!_initialised) {
            this.initialise();
            _initialised = true;
        }
    }

    public abstract void checkMultipleMoveAction(CheckMultipleMovesResponse response);

    public void hostAction(HostResponse response) {
        _game = response.game;
        _player =  new Player(response.playerId);
    }

    public void joinAction(JoinResponse response) {
        _game = response.game;
        _player =  new Player(response.playerId);

        if (_player.isHost() && _game.players.size() == 5) {
            _client.Send(new Message<Request>(MessageType.START, _player.username(), new Request(_game.name)));
        }
    }

    public void leaveAction(LeaveResponse response) {
        if (response.hasLeft) {
            _player = null;
            _game = null;
        }
        else {
            _game = response.game;
        }
    }

    public void newGameAction(NewGameResponse response) {
        _avaliableGameNames.add(response.gameName);
    }

    public void playerMoveAction(PlayerMoveResponse response) {
        _game = response.game;
        this.startMove();
    }

    public void startAction(StartResponse response) {
        _game = response.game;
        
        this.startMove();
    }

    private void startMove() {
        if (_game.playerTurn == _player.id) {
            try {
                this.makeMove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}