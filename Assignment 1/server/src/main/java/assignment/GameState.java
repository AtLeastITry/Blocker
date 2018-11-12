package assignment;

import java.util.*;

// This class (not yet fully implemented) will give access to the current state of the game.
public final class GameState {
    public static final int ROWS = 6;
    public static final int COLUMNS = 10;
    public String name;
    private ArrayList<UserPlayer> _userPlayers;
    private int[][] _board;
    private Map<Integer, Set<InfluenceCard>> _influenceCards;
    private boolean _inProgress;
    private int _playerTurn;
    private boolean _gameFinished;
    
    public GameState(String name) {
        _userPlayers = new ArrayList<>();
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
        int playersLost = 0;
        for (int i = 0; i < this._userPlayers.size(); i++) {
            UserPlayer user = _userPlayers.get(i);
            if (!user.canMove) {
                playersLost++;
                continue;
            }
            boolean canMove = false;
            Set<InfluenceCard> cards = this.getAvailableInfluenceCards(user.playerId);
            boolean hasFreedom = false;
            boolean hasReplacement = false;

            if (cards.contains(InfluenceCard.FREEDOM)) {
               hasFreedom = true;
            }
            else if (cards.contains(InfluenceCard.REPLACEMENT)) {
                hasReplacement = true;
            }

            rowLoop:
            for (int x = 0; x < this.getBoard().length; x++) {
                for (int y = 0; y < this.getBoard()[i].length; y++) {
                    Integer stoneId = getStoneId(new Coordinates(x, y));

                    if (hasFreedom && stoneId == 0) {
                        canMove = this.checkMove(x, y, InfluenceCard.FREEDOM, user.playerId);
                    }
                    else if(hasReplacement && stoneId != 0) {
                        canMove = this.checkMove(x, y, InfluenceCard.REPLACEMENT, user.playerId);
                    }
                    else if(stoneId == 0) {
                        canMove = this.checkMove(x, y, null, user.playerId);
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

    private boolean checkMove(int x, int y, InfluenceCard card, int playerId) {
        Move move = new Move(card, new Coordinates(x, y), null);
        return this.isMoveAllowed(move, playerId);
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

        boolean canMoveX = next.getX() == current.getX() - 1 || next.getX() == current.getX() + 1 || next.getX() == current.getX();
        boolean canMoveY = next.getY() == current.getY() - 1 || next.getY() == current.getY() + 1 || next.getY() == current.getY();

        boolean sameMove = next.getX() == current.getX() && next.getY() == current.getY();

        return !sameMove && canMoveX && canMoveY;
    }

    private boolean validCoordinate(int playerId, Coordinates next, InfluenceCard card) {
        Integer stoneId = getStoneId(next);
        
        if (stoneId == null) {
            return false;
        }

        if (stoneId != null && stoneId != 0 && card != InfluenceCard.REPLACEMENT) {
            return false;
        }

        if (card == InfluenceCard.FREEDOM && stoneId == 0) 
        {
            return true;
        }

        Integer right = getStoneId(new Coordinates(next.getX(),next.getY() + 1));
        Integer rightUp = getStoneId(new Coordinates(next.getX() - 1,next.getY() + 1));
        Integer rightDown = getStoneId(new Coordinates(next.getX() + 1,next.getY() + 1));        
        Integer left = getStoneId(new Coordinates(next.getX(),next.getY() - 1));
        Integer leftUp = getStoneId(new Coordinates(next.getX() -1 ,next.getY() - 1));
        Integer leftDown = getStoneId(new Coordinates(next.getX() + 1,next.getY() - 1));
        Integer up = getStoneId(new Coordinates(next.getX() - 1,next.getY()));
        Integer down = getStoneId(new Coordinates(next.getX() + 1,next.getY()));        
        
        boolean isRightMove = right != null && right == playerId;
        boolean isRightUpMove = rightUp != null && rightUp == playerId;
        boolean isRightDownMove = rightDown != null && rightDown == playerId;
        boolean isLeftMove = left != null && left == playerId;
        boolean isLeftUpMove = leftUp != null && leftUp == playerId;
        boolean isLeftDownMove = leftDown != null && leftDown == playerId;
        boolean isUpMove = up != null && up == playerId;
        boolean isDownMove = down != null && down == playerId;
        

        return isRightMove || isRightUpMove || isRightDownMove || isLeftMove || isLeftUpMove || isLeftDownMove || isUpMove || isDownMove;
    }

    // Checks if the specified move is allowed for the given player.
    public boolean isMoveAllowed(Move move, int player) {
        boolean validSecondMove = true;

        if (move.getSecondMove() != null) {
            validSecondMove = validCoordinates(player, move.getFirstMove(), move.getSecondMove(), move.getCard()) || validCoordinate(player, move.getSecondMove(), move.getCard());
        }

        if (!validSecondMove) {
            return false;
        }

        return validCoordinate(player, move.getFirstMove(), move.getCard());
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

