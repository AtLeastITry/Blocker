package Bot.ai;

import java.net.URISyntaxException;

import Bot.models.Move;

public class MinMaxAgent extends BaseAgent {
    public MinMaxAgent() throws URISyntaxException {
        super();
    }

    @Override
    public Move calculateNextMove() {
        return new Move();
    }

    @Override
    public void moveResult(boolean moveAllowed, Move move) {

    }
}