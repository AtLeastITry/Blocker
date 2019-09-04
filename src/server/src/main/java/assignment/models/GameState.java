package assignment.models;

import java.util.*;

import assignment.services.MoveValidator;

// This class (not yet fully implemented) will give access to the current state of the game.
public final class GameState {
    public static final int ROWS = 6;
    public static final int COLUMNS = 10;
    public String name;
    private ArrayList<UserPlayer> _userPlayers;
    private ArrayList<String> _spectators; 
    private int[][] _board;
    private Map<Integer, Set<InfluenceCard>> _influenceCards;
    private boolean _inProgress;
    private int _playerTurn;
    private boolean _gameFinished;
    
    public GameState(String name) {
        _userPlayers = new ArrayList<>();
        _spectators = new ArrayList<>();
        _board = new int[6][10];
        _influenceCards = new HashMap<>();
        _inProgress = false;
        this.name = name;
    }

    public void start() {
        _inProgress = true;
        _playerTurn = 1;
        Random r = new Random();
        for (UserPlayer player: _userPlayers) {
            boolean stoneSet = false;
            while(!stoneSet) {
                int low = 0;
                int high = 6;
                int x = r.nextInt(high-low) + low;
                high = 10;
                int y = r.nextInt(high-low) + low;

                if (_board[x][y] == 0) {
                    _board[x][y] = player.playerId;
                    stoneSet = true;
                }
            }

        }
    }

    public boolean getInPogress() {
        return this._inProgress;
    }

    public boolean checkPlayerInGame(String sessionId) {
        boolean containsPlayer = false;
        for (UserPlayer player: _userPlayers) {
            if(player.sessionId.equals(sessionId)) {
                containsPlayer = true;
                break;
            }
        }

        for (String spectator: _spectators) {
            if(spectator.equals(sessionId)) {
                containsPlayer = true;
                break;
            }
        }

        return containsPlayer;
    }

    public Integer getPlayerId(String sessionId) {
        Integer result = null;
        for (UserPlayer player: _userPlayers) {
            if(player.sessionId.equals(sessionId)) {
                result = player.playerId;
                break;
            }
        }

        return result;
    }

    public ArrayList<UserPlayer> getUserPlayers() {
        return this._userPlayers;
    }

    public void checkPlayersCanMove() {
        MoveValidator validator = new MoveValidator(this);

        int playersLost = 0;
        for (int i = 0; i < this._userPlayers.size(); i++) {
            UserPlayer user = _userPlayers.get(i);
            boolean canMove = false;
            Set<InfluenceCard> cards = this.getAvailableInfluenceCards(user.playerId);
            boolean hasFreedom = cards.contains(InfluenceCard.FREEDOM);
            boolean hasReplacement = cards.contains(InfluenceCard.REPLACEMENT);

            rowLoop:
            for (int x = 0; x < this.getBoard().length; x++) {
                for (int y = 0; y < this.getBoard()[i].length; y++) {
                    Integer stoneId = getStoneId(x, y);

                    if (hasFreedom && stoneId == 0) {
                        canMove = validator.validate(x, y, InfluenceCard.FREEDOM, user.playerId);
                    }
                    else if(hasReplacement && stoneId != 0 && stoneId != user.playerId) {
                        canMove = validator.validate(x, y, InfluenceCard.REPLACEMENT, user.playerId);
                    }
                    else if(stoneId == 0) {
                        canMove = validator.validate(x, y, null, user.playerId);
                    }

                    if (canMove == true) {
                        break rowLoop;
                    }
                }
            }
            if (!canMove) {
                playersLost++;
            }
            user.canMove = canMove;
        }

        if (playersLost == this.getNumberOfPlayers() - 1) {
            this._gameFinished = true;
        }
    }

    

    public void addInfluenceCards(int playerId) {
        HashSet<InfluenceCard> cards = new HashSet<>();
        cards.add(InfluenceCard.DOUBLE);
        cards.add(InfluenceCard.REPLACEMENT);
        cards.add(InfluenceCard.FREEDOM);
        this._influenceCards.put(playerId, cards);
    }

    public void removePlayerInfluenceCards(int playerId) {
        this._influenceCards.remove(playerId);
    }

    public void removeInfluenceCard(int playerId, InfluenceCard card) {
        Set<InfluenceCard> existingCards = this._influenceCards.get(playerId);
        Set<InfluenceCard> newCards = new HashSet<>();

        for (InfluenceCard exisitngCard: existingCards) {
            if (exisitngCard != card) {
                newCards.add(exisitngCard);
            }
        }

        this._influenceCards.remove(playerId);
        this._influenceCards.put(playerId, newCards);
    }
    
    public void addPlayer(String sessionId, Player player) {
        player.setMyPlayerId(this.getNumberOfPlayers() + 1);
        this._userPlayers.add(new UserPlayer(player.getMyPlayerId(), sessionId));
        this.addInfluenceCards(player.getMyPlayerId());
    }

    public void addSpectator(String sessionId) {
        this._spectators.add(sessionId);
    }

    public void removeSpectator(String sessionId) {
        this._spectators.remove(sessionId);
    }

    public void removePlayer(int playerId) {
        this._userPlayers.removeIf(userPlayer -> userPlayer.playerId == playerId);
        this.removePlayerInfluenceCards(playerId);
    }

    public int getNumberOfPlayers() {
        return this._userPlayers.size();
    }

    public ArrayList<UserPlayer> getActivePlayers() {
        ArrayList<UserPlayer> players = new ArrayList<>();

        for (UserPlayer player: this._userPlayers) {
            if (player.canMove) {
                players.add(player);
            }
        }

        return players;
    }

    // Returns a rectangular matrix of board cells, with six rows and ten columns.
    // Zeros indicate empty cells.
    // Non-zero values indicate stones of the corresponding player.  E.g., 3 means a stone of the third player.
    public int[][] getBoard() {
        return _board;
    }

    public void updateBoard(Coordinates coordinates, int playerId) {
        this._board[coordinates.getX()][coordinates.getY()] = playerId;
    }

    // Returns the set of influence cards available to the given player.
    public Set<InfluenceCard> getAvailableInfluenceCards(int player) {
        return _influenceCards.get(player);
    }

    public Integer getStoneId(int x, int y) {
        if (x < 0 || x > 5) {
            return null;
        }

        if (y < 0 || y > 9) {
            return null;
        }

        return this.getBoard()[x][y];
    }

    public int getNextPlayerTurn(int playerId, ArrayList<UserPlayer> activePlayers) {
        if (activePlayers.size() < 1) {
            return playerId;
        }

        if (playerId == this.getNumberOfPlayers()) {
            return getNextPlayerTurn(0, activePlayers);
        }

        playerId++;

        for (UserPlayer player : activePlayers) {
            if (player.playerId == playerId) {
                return playerId;
            }
        }

        return getNextPlayerTurn(playerId, activePlayers);
    }

    public void makeMoves(Move move, int playerId) {
        this.updateBoard(move.getFirstMove(), playerId);

        if (move.getSecondMove() != null) {
            this.updateBoard(move.getSecondMove(), playerId);
        }

        if (move.getCard() != null && move.getCard() != InfluenceCard.NONE) {
            this.removeInfluenceCard(playerId, move.getCard());
        }

        this.checkPlayersCanMove();
        ArrayList<UserPlayer> activePlayers = getActivePlayers();
        _playerTurn = getNextPlayerTurn(_playerTurn, activePlayers);    
    }
}

