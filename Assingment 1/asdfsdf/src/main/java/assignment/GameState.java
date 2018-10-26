package assignment;

import java.util.*;

// This class (not yet fully implemented) will give access to the current state of the game.
public final class GameState {
    public static final int ROWS = 6;
    public static final int COLUMNS = 10;
    private Set<String> _playerSessionIds;
    private int[][] _board;
    private Map<Integer, Set<InfluenceCard>> _influencecards;
    
    public GameState() {
        this._playerSessionIds = new HashSet<>();
        _board = new int[6][10];
        _influencecards = new HashMap<>();
    }
    
    public void addPlayer(String sessionId) {
        this._playerSessionIds.add(sessionId);
    }
    public void removePlayer(String sessionId) {
        this._playerSessionIds.removeIf(id -> id == sessionId);
    }

    public int getNumberOfPlayers() {
        return this._playerSessionIds.size();
    }

    // Returns a rectangular matrix of board cells, with six rows and ten columns.
    // Zeros indicate empty cells.
    // Non-zero values indicate stones of the corresponding player.  E.g., 3 means a stone of the third player.
    public int[][] getBoard() {
        return _board;
    }

    // Returns the set of influence cards available to the given player.
    public Set<InfluenceCard> getAvailableInfluenceCards(int player) {
        return _influencecards.get(player);
    }

    private Integer getStoneId(Coordinates current) {
        if (current.getX() < 0 || current.getX() > 9) {
            return null;
        }

        if (current.getY() > 0 || current.getY() > 5) {
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
        if (getStoneId(next) != 0 && card != InfluenceCard.REPLACEMENT) {
            return false;
        }

        if (card == InfluenceCard.FREEDOM || isFirstMove) {
            return true;
        }

        boolean isDownMove = getStoneId(new Coordinates(next.getX(),next.getY() + 1)) == playerId;
        boolean isUpMove = getStoneId(new Coordinates(next.getX(),next.getY() - 1)) == playerId;
        boolean isLeftMove = getStoneId(new Coordinates(next.getX() - 1,next.getY())) == playerId;
        boolean isRightMove = getStoneId(new Coordinates(next.getX() - 1,next.getY() + 1)) == playerId;

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
}

