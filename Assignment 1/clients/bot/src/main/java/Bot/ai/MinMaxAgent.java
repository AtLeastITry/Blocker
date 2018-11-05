package Bot.ai;

import java.net.URISyntaxException;

import Bot.models.Move;
import Bot.models.Responses.CheckMultipleMovesResponse;

public class MinMaxAgent extends BaseAgent {
    public MinMaxAgent() throws URISyntaxException {
        super();
    }

    @Override
    public Move calculateNextMove() {
        return new Move();
    }

    @Override
    public void checkMultipleMoveAction(CheckMultipleMovesResponse response) {

	}
}