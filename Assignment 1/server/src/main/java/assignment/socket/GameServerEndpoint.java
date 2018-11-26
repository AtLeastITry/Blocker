package assignment.socket;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import assignment.domain.GameHandler;
import assignment.models.GameState;
import assignment.models.Message;
import assignment.models.MessageType;
import assignment.models.Player;
import assignment.util.MessageDecoder;
import assignment.util.MessageEncoder;
import com.google.gson.Gson;

import assignment.models.responses.GameRemovedResponse;

import java.io.IOException;
import java.util.ArrayList;


@ServerEndpoint(value = "/game", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class GameServerEndpoint {

    private final static Context _context = new Context();
    private final GameHandler _handler = new GameHandler(_context);

    @OnOpen
    public void open(Session session) {
        _context.users.add(session);
    }

    @OnClose
    public void close(final Session session) throws IOException, EncodeException {
        Player temp = null;

        for (Player player: _context.players) {
            if (player.getSessionId() == session.getId()) {
                temp = player;
            }
        }

        _context.players.removeIf(player -> player.getSessionId() == session.getId());

        ArrayList<String> gamesToRemove = new ArrayList<>();

        for (GameState gameState: _context.games) {
            if (gameState.checkPlayerInGame(session.getId())) {
                if (temp != null) {
                    gameState.removePlayer(temp.getMyPlayerId());
                    if (gameState.getNumberOfPlayers() == 0) {
                        gamesToRemove.add(gameState.name);
                    }
                }
                gameState.removeSpectator(session.getId());
            }
        }
        _context.users.remove(session);
        if (gamesToRemove.size() > 0) {
            _context.games.removeIf(game -> gamesToRemove.contains(game.name));
            Message reply = new Message();
            reply.type = MessageType.GAME_REMOVED;
            reply.sender = "Server";
            reply.data = new Gson().toJson(new GameRemovedResponse(true, gamesToRemove.get(0)));

            for (Session user: _context.users) {
                user.getBasicRemote().sendObject(reply);
            }
        }        
    }

    @OnError
    public void onError(Throwable error) {
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        switch(message.type) {
            case MessageType.HOST:
                _handler.handleHostRequest(session, message);
                break;
            case MessageType.PLAYER_MOVE:
                _handler.handleMakeMoveRequest(session, message);
                break;
            case MessageType.ALL_GAMES:
                _handler.handleAllGamesRequest();
                break;
            case MessageType.START:
                _handler.handleStartRequest(session, message);
                break;
            case MessageType.JOIN:
                _handler.handleJoinRequest(session, message);
                break;
            case MessageType.CHECK_MOVE:
                _handler.handleCheckMoveRequest(session, message);
                break;
            case MessageType.CHECK_MULTIPLE_MOVES:
                _handler.handleCheckMultipleMovesRequest(session, message);
                break;
            case MessageType.LEAVE:
                _handler.handleLeaveRequest(session, message);
                break;
            case MessageType.SPECTATE_GAME:
                _handler.handleSpectateRequest(session, message);
                break;
            case MessageType.GAMES_IN_PROGRESS:
                _handler.handleInProgressRequest(session);
                break;
        }
    }
}
