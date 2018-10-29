package assignment;

import java.util.*;

// This class (not yet fully implemented) will give access to the current state of the game.
public final class GameState {
    public static final int ROWS = 6;
    public static final int COLUMNS = 10;
    public final String name = UUID.randomUUID().toString();
    private Set<UserPlayer> _userPlayers;
    private int[][] _board;
    private Map<Integer, Set<InfluenceCard>> _influenceCards;
    private boolean _inProgress;
    private int _playerTurn;


    
    public GameState() {
        _userPlayers = new HashSet<>();
        _board = new int[6][10];
        _influenceCards = new HashMap<>();
        _inProgress = false;
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

    public Set<UserPlayer> getUserPlayers() {
        return this._userPlayers;
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

        this._influenceCards.put(playerId, newCards);
    }
    
    public void addPlayer(String sessionId, Player player) {
        player.setMyPlayerId(this.getNumberOfPlayers() + 1);
        this._userPlayers.add(new UserPlayer(player.getMyPlayerId(), sessionId));
        this.addInfluenceCards(player.getMyPlayerId());
    }
    public void removePlayer(int playerId) {
        this._userPlayers.removeIf(userPlayer -> userPlayer.playerId == playerId);
        this.removePlayerInfluenceCards(playerId);
    }

    public int getNumberOfPlayers() {
        return this._userPlayers.size();
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

    private Integer getStoneId(Coordinates current) {
        if (current.getX() < 0 || current.getX() > 5) {
            return null;
        }

        if (current.getY() < 0 || current.getY() > 9) {
            return null;
        }

        return this.getBoard()[current.getX()][current.getY()];
    }


    private boolean validCoordinates(int playerId, Coordinates current, Coordinates next, InfluenceCard card) {
        if (getStoneId(next) != 0 && card != InfluenceCard.REPLACEMENT) {
            return false;
        }

        boolean canMoveX = next.getX() != current.getX() - 1 || next.getX() != current.getX() + 1;
        boolean canMoveY = next.getY() != current.getY() - 1 || next.getY() != current.getY() + 1;

        return canMoveX && canMoveY;
    }

    private boolean validCoordinate(int playerId, Coordinates next, boolean isFirstMove, InfluenceCard card) {
        Integer stoneId = getStoneId(next);
        if (stoneId != null && stoneId != 0 && card != InfluenceCard.REPLACEMENT) {
            return false;
        }

        if (card == InfluenceCard.FREEDOM || isFirstMove) {
            return true;
        }

        Integer down = getStoneId(new Coordinates(next.getX(),next.getY() + 1));
        Integer up = getStoneId(new Coordinates(next.getX(),next.getY() - 1));
        Integer left = getStoneId(new Coordinates(next.getX() - 1,next.getY()));
        Integer right = getStoneId(new Coordinates(next.getX() + 1,next.getY()));
        boolean isDownMove = down != null && down == playerId;
        boolean isUpMove = up != null && up == playerId;
        boolean isLeftMove = left != null && left == playerId;
        boolean isRightMove = right != null && right == playerId;

        return isDownMove || isUpMove || isLeftMove || isRightMove;
    }

    // Checks if the specified move is allowed for the given player.
    public boolean isMoveAllowed(Move move, int player) {
        if (move.getSecondMove() != null) {
            if (!validCoordinates(player, move.getFirstMove(), move.getSecondMove(), move.getCard())) {
                return false;
            }
        }

        boolean isFirstMove = true;

        rowLoop:
        for (int[] row: this.getBoard()) {
            for (int column: row) {
                if (column == player) {
                    isFirstMove = false;
                    break rowLoop;
                }
            }
        }

        return validCoordinate(player, move.getFirstMove(), isFirstMove, move.getCard());
    }

    public void makeMoves(Move move, int playerId) {
        this.updateBoard(move.getFirstMove(), playerId);
        if (move.getSecondMove() != null) {
            this.updateBoard(move.getSecondMove(), playerId);
        }

        if (move.getCard() != null && move.getCard() != InfluenceCard.NONE) {
            this.removeInfluenceCard(playerId, move.getCard());
        }

        if (playerId == this.getNumberOfPlayers()) {
            _playerTurn = 1;
        }
        else {
            _playerTurn++;
        }
    }
}

