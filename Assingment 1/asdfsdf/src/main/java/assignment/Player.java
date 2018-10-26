package assignment;

public class Player implements PlayerLogic {
    private String _sessionId;
    private int _playerId;
    private Move _nextMove;
    private String _username;

    public Player(int id) {
        _playerId = id;
    }

    public String getUsernmae() {
        return this._username;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public Move getNextMove() {
        return this._nextMove;
    }

    public void setNextMove(Move move) {
        this._nextMove = move;
    }

    public void setSessionId(String id) {
        this._sessionId = id;
    }

    public String getSessionId() {
        return this._sessionId;
    }

    public void setPlayerId(int id) {
        this._playerId = id;
    }

    @Override
    public int getMyPlayerId() {
        return _playerId;
    }

    @Override
    public Move makeMove(GameState game) {
        if (game.isMoveAllowed(this.getNextMove(), this.getMyPlayerId())) {
            return this.getNextMove();
        }
        else {
            return null;
        }
    }
}
