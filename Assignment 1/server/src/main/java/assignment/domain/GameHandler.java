package assignment.domain;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.google.gson.Gson;

import assignment.domain.commands.game.all.AllCommand;
import assignment.domain.commands.game.all.AllRequest;
import assignment.domain.commands.game.checkMove.CheckMoveCommand;
import assignment.domain.commands.game.checkMove.CheckMoveCommandRequest;
import assignment.domain.commands.game.checkMultipleMoves.CheckMultipleMovesCommand;
import assignment.domain.commands.game.checkMultipleMoves.CheckMultipleMovesCommandRequest;
import assignment.domain.commands.game.host.HostCommand;
import assignment.domain.commands.game.host.HostRequest;
import assignment.domain.commands.game.inProgress.InProgressCommand;
import assignment.domain.commands.game.inProgress.InProgressRequest;
import assignment.domain.commands.game.join.JoinCommand;
import assignment.domain.commands.game.join.JoinRequest;
import assignment.domain.commands.game.leave.LeaveCommand;
import assignment.domain.commands.game.leave.LeaveCommandRequest;
import assignment.domain.commands.game.makeMove.MakeMoveCommand;
import assignment.domain.commands.game.makeMove.MakeMoveRequest;
import assignment.domain.commands.game.spectate.SpectateCommand;
import assignment.domain.commands.game.spectate.SpectateCommandRequest;
import assignment.domain.commands.game.start.StartCommand;
import assignment.domain.commands.game.start.StartRequest;
import assignment.models.Message;
import assignment.models.requests.CheckMoveRequest;
import assignment.models.requests.CheckMultipleMovesRequest;
import assignment.models.requests.PlayerMoveRequest;
import assignment.models.requests.Request;
import assignment.socket.Context;

public class GameHandler {
    private Context _context;

    public GameHandler(Context context) {
        _context = context;
    }

    public void handleHostRequest(Session session, Message message) throws IOException, EncodeException {
        new HostCommand(_context).executeAsync(new HostRequest(session));
    }

    public void handleJoinRequest(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        new JoinCommand(_context).executeAsync(new JoinRequest(session.getId(), request.gameName));
    }

    public void handleStartRequest(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        new StartCommand(_context).executeAsync(new StartRequest(session.getId(), request.gameName));
    }

    public void handleLeaveRequest(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        new LeaveCommand(_context).executeAsync(new LeaveCommandRequest(session, request.gameName));
    }

    public void handleAllGamesRequest() {
        new AllCommand(_context).executeAsync(new AllRequest());
    }

    public void handleSpectateRequest(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        new SpectateCommand(_context).executeAsync(new SpectateCommandRequest(session, request.gameName));
    }

    public void handleInProgressRequest(Session session) throws IOException, EncodeException {
        new InProgressCommand(_context).executeAsync(new InProgressRequest(session));
    }

    public void handleMakeMoveRequest(Session session, Message message) throws IOException, EncodeException {
        PlayerMoveRequest request = new Gson().fromJson(message.data, PlayerMoveRequest.class);
        new MakeMoveCommand(_context).executeAsync(new MakeMoveRequest(session, request.gameName, request.move));
    }

    public void handleCheckMoveRequest(Session session, Message message) {
        CheckMoveRequest request = new Gson().fromJson(message.data, CheckMoveRequest.class);
        new CheckMoveCommand(_context).executeAsync(new CheckMoveCommandRequest(session, request.gameName, request.move));
    }

    public void handleCheckMultipleMovesRequest(Session session, Message message) {
        CheckMultipleMovesRequest request = new Gson().fromJson(message.data, CheckMultipleMovesRequest.class);
        new CheckMultipleMovesCommand(_context).executeAsync(new CheckMultipleMovesCommandRequest(session, request.gameName, request.moves));
    }
}
