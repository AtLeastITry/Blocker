package Bot.ai;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import Bot.models.*;
import Bot.models.Requests.*;
import Bot.models.Responses.*;

public class RandomAgent extends BaseAgent {
    public RandomAgent() throws URISyntaxException {
        super();
    }

    @Override
    public Move calculateNextMove() {
        Move move = new Move();

        ArrayList<Coordinates> currentPositions = new ArrayList<>();

        for (int i = 0; i < _game.board.length; i++) {
            int[] row = _game.board[i];

            for (int j = 0; j < row.length; j++) {
                int column = row[j];

                if (column == _player.id) {
                    currentPositions.add(new Coordinates(i, j));
                }
            }
        }

        ArrayList<Move> movesToCheck = new ArrayList<>();

        for(Coordinates coordinates: currentPositions) {
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

        return move;
    }

    @Override
    public void checkMultipleMoveAction(CheckMultipleMovesResponse response) {
        ArrayList<Move> movesAllowed = new ArrayList<>();
        for (MoveResponse move : response.moves) {
            if (move.allowed) {
                movesAllowed.add(move.move);
            }
        }

        Random r = new Random();

        int moveIndex = r.nextInt(movesAllowed.size());

        Move moveToTake = movesAllowed.get(moveIndex);

        _client.Send(new Message<MoveRequest>(MessageType.PLAYER_MOVE, _player.username(), new MoveRequest(_game.name, moveToTake)));
	}
}