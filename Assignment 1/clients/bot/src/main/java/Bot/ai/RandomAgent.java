package Bot.ai;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Bot.models.Coordinates;
import Bot.models.InfluenceCard;
import Bot.models.Message;
import Bot.models.MessageType;
import Bot.models.Move;
import Bot.models.Requests.CheckMultipleMovesRequest;
import Bot.models.Requests.MoveRequest;
import Bot.models.Responses.CheckMultipleMovesResponse;
import Bot.models.Responses.MoveResponse;

public class RandomAgent extends BaseAgent {
    public RandomAgent() throws URISyntaxException {
        super();
    }

    private final Map<Integer, ArrayList<Coordinates>> positionsCache = new HashMap<>();

    @Override
    public void triggerMove() {
        Move move = new Move();
        positionsCache.clear();
        for (int i = 0; i < _game.board.length; i++) {
            int[] row = _game.board[i];

            for (int j = 0; j < row.length; j++) {
                int column = row[j];

                ArrayList<Coordinates> temp = new ArrayList<>();

                if(positionsCache.containsKey(column)) {
                    temp.addAll(positionsCache.get(column));
                }

                temp.add(new Coordinates(i, j));

                positionsCache.put(column, temp);
            }
        }

        ArrayList<Coordinates> currentCoordinates = positionsCache.get(_player.id);
        ArrayList<Move> movesToCheck =  new ArrayList<>();
        for(Coordinates coordinates: currentCoordinates) {
            Move downMove = new Move();
            downMove.firstMove = new Coordinates(coordinates.x + 1, coordinates.y);
            Move upMove = new Move();
            upMove.firstMove = new Coordinates(coordinates.x - 1, coordinates.y);
            Move leftMove = new Move();
            leftMove.firstMove = new Coordinates(coordinates.x, coordinates.y - 1);
            Move rightMove = new Move();
            rightMove.firstMove = new Coordinates(coordinates.x, coordinates.y + 1);
            
            movesToCheck.add(downMove);
            movesToCheck.add(upMove);
            movesToCheck.add(leftMove);
            movesToCheck.add(rightMove);
        }

        _client.Send(new Message<CheckMultipleMovesRequest>(MessageType.CHECK_MULTIPLE_MOVES, _player.username(), new CheckMultipleMovesRequest(_game.name, movesToCheck, _player.id)));      
    }

    @Override
    public void checkMultipleMoveAction(CheckMultipleMovesResponse response) {
        ArrayList<Move> movesAllowed =  new ArrayList<>();
        for (MoveResponse move : response.moves) {
            if (move.allowed) {
                movesAllowed.add(move.move);
            }
        }

        Random r = new Random();

        if (movesAllowed.size() < 1) {
            Set<InfluenceCard> cards = _game.getPlayerCards(_player.id);
            ArrayList<Coordinates> freeCoordinates = positionsCache.get(0);

            if (freeCoordinates != null && freeCoordinates.size() > 0 && cards.contains(InfluenceCard.FREEDOM)) {
                Move moveToTake = new Move();                
                int coordinatesIndex = r.nextInt(freeCoordinates.size());
                moveToTake.card = InfluenceCard.FREEDOM;
                moveToTake.firstMove = freeCoordinates.get(coordinatesIndex);
                _client.Send(new Message<MoveRequest>(MessageType.PLAYER_MOVE, _player.username(), new MoveRequest(_game.name, moveToTake)));

                return;
            }
            else if (cards.contains(InfluenceCard.REPLACEMENT)) {
                Move moveToTake = new Move();
                int moveIndex = r.nextInt(response.moves.size());
                MoveResponse temp = response.moves.get(moveIndex);
                moveToTake.card = InfluenceCard.REPLACEMENT;
                moveToTake.firstMove = temp.move.firstMove;
                _client.Send(new Message<MoveRequest>(MessageType.PLAYER_MOVE, _player.username(), new MoveRequest(_game.name, moveToTake)));

                return;
            }
        }
        else {
            Move moveToTake = new Move();
            int moveIndex = r.nextInt(movesAllowed.size());    
            moveToTake = movesAllowed.get(moveIndex);
            movesAllowed.clear();
            _client.Send(new Message<MoveRequest>(MessageType.PLAYER_MOVE, _player.username(), new MoveRequest(_game.name, moveToTake)));

            return;
        }
	}
}