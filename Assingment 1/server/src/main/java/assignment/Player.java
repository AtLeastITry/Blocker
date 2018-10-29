package assignment;

public class Player implements PlayerLogic {
    private String _sessionId;
    private int _playerId;
    private Move _nextMove;

    public Player(String sessionId) {
        this.setSessionId(sessionId);
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

    public void setMyPlayerId(int id) {
        _playerId = id;
    }

    @Override
    public int getMyPlayerId() {
        return _playerId;
    }

    @Override
    public Move makeMove(GameState game) {
        if (game.isMoveAllowed(this.getNextMove(), this.getMyPlayerId())) {
            game.makeMoves(this.getNextMove(), this.getMyPlayerId());
            return this.getNextMove();
        }
        else {
            return null;
        }
    }
}
