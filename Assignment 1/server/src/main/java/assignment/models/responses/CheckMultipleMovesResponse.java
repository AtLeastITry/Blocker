package assignment.models.responses;

import java.util.ArrayList;

public class CheckMultipleMovesResponse extends Response {
    public ArrayList<MoveResponse> moves;

    public CheckMultipleMovesResponse(boolean success, ArrayList<MoveResponse> moves) {
        super(success);

        this.moves = moves;
    }
}