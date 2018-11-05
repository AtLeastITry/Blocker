package Bot.ai;

import Bot.models.Responses.*;

public interface IAgent {
    public void run();
    public void makeMove() throws InterruptedException;

    public void allGamesAction(AllGamesResponse response);
    public void checkMultipleMoveAction(CheckMultipleMovesResponse response);
    public void hostAction(HostResponse response);
    public void joinAction(JoinResponse response);
    public void leaveAction(LeaveResponse response);
    public void newGameAction(NewGameResponse response);
    public void playerMoveAction(PlayerMoveResponse response);
    public void startAction(StartResponse response);
}