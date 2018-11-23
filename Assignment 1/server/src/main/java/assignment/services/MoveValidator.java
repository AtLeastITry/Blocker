package assignment.services;

import assignment.models.Coordinates;
import assignment.models.GameState;
import assignment.models.InfluenceCard;
import assignment.models.Move;

public class MoveValidator {
    private GameState _game;

    public MoveValidator(GameState game) {
        _game = game;
    }

    public boolean validate(Move move, int player) {
        boolean validSecondMove = true;

        if (move.getSecondMove() != null) {
            validSecondMove = validCoordinates(player, move.getFirstMove(), move.getSecondMove(), move.getCard()) 
            || validCoordinate(player, move.getSecondMove(), move.getCard());
        }

        if (!validSecondMove) {
            return false;
        }

        return validCoordinate(player, move.getFirstMove(), move.getCard());
    }

    public boolean validate(int x, int y, InfluenceCard card, int playerId) {
        Move move = new Move(card, new Coordinates(x, y), null);
        return this.validate(move, playerId);
    }

    private boolean validCoordinates(int playerId, Coordinates current, Coordinates next, InfluenceCard card) {
        if (_game.getStoneId(next.getX(), next.getY()) != 0 && card != InfluenceCard.REPLACEMENT) {
            return false;
        }

        boolean canMoveX = next.getX() == current.getX() - 1 || next.getX() == current.getX() + 1 || next.getX() == current.getX();
        boolean canMoveY = next.getY() == current.getY() - 1 || next.getY() == current.getY() + 1 || next.getY() == current.getY();

        boolean sameMove = next.getX() == current.getX() && next.getY() == current.getY();

        return !sameMove && canMoveX && canMoveY;
    }

    private boolean validCoordinate(int playerId, Coordinates next, InfluenceCard card) {
        Integer stoneId = _game.getStoneId(next.getX(), next.getY());
        
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

        Integer right = _game.getStoneId(next.getX(),next.getY() + 1);
        Integer rightUp = _game.getStoneId(next.getX() - 1,next.getY() + 1);
        Integer rightDown = _game.getStoneId(next.getX() + 1,next.getY() + 1);        
        Integer left = _game.getStoneId(next.getX(),next.getY() - 1);
        Integer leftUp = _game.getStoneId(next.getX() -1 ,next.getY() - 1);
        Integer leftDown = _game.getStoneId(next.getX() + 1,next.getY() - 1);
        Integer up = _game.getStoneId(next.getX() - 1,next.getY());
        Integer down = _game.getStoneId(next.getX() + 1,next.getY());        
        
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
}