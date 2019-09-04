package Bot.models.Requests;

import Bot.models.Move;

public class MoveRequest extends Request {
    public Move move;

    public MoveRequest(String gameName, Move move) {
        super(gameName);
        
        this.move = move;
    }
}