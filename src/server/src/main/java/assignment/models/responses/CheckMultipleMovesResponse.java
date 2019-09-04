package assignment.models.responses;

import java.util.List;

public class CheckMultipleMovesResponse extends Response {
    public List<MoveResponse> moves;

    public CheckMultipleMovesResponse(boolean success, List<MoveResponse> moves) {
        super(success);

        this.moves = moves;
    }
}