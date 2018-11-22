package assignment;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import assignment.models.GameRemovedResponse;

import java.io.IOException;
import java.util.ArrayList;


@ServerEndpoint(value = "/game", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class GameServerEndpoint {

    private static GameService _gameService = new GameService();

    @OnOpen
    public void open(Session session) {
        _gameService.users.add(session);
    }

    @OnClose
    public void close(final Session session) throws IOException, EncodeException {
        Player temp = null;

        for (Player player: _gameService.players) {
            if (player.getSessionId() == session.getId()) {
                temp = player;
            }
        }

        _gameService.players.removeIf(player -> player.getSessionId() == session.getId());

        ArrayList<String> gamesToRemove = new ArrayList<>();

        for (GameState gameState: _gameService.games) {
            if (gameState.checkPlayerInGame(session.getId())) {
                gameState.removePlayer(temp.getMyPlayerId());
                if (gameState.getNumberOfPlayers() == 0) {
                    gamesToRemove.add(gameState.name);
                }
            }
        }
        _gameService.users.remove(session);
        if (gamesToRemove.size() > 0) {
            _gameService.games.removeIf(game -> gamesToRemove.contains(game.name));
            Message reply = new Message();
            reply.type = MessageType.GAME_REMOVED;
            reply.sender = "Server";
            reply.data = new Gson().toJson(new GameRemovedResponse(true, gamesToRemove.get(0)));

            for (Session user: _gameService.users) {
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
                _gameService.host(session, message);
                break;
            case MessageType.PLAYER_MOVE:
                _gameService.playerMove(session, message);
                break;
            case MessageType.ALL_GAMES:
                _gameService.allGames();
                break;
            case MessageType.START:
                _gameService.start(session, message);
                break;
            case MessageType.JOIN:
                _gameService.join(session, message);
                break;
            case MessageType.CHECK_MOVE:
                _gameService.checkMove(session, message);
                break;
            case MessageType.CHECK_MULTIPLE_MOVES:
                _gameService.checkMultipleMoves(session, message);
                break;
            case MessageType.LEAVE:
                _gameService.leave(session, message);
                break;
            case MessageType.SPECTATE_GAME:
                _gameService.spectate(session, message);
                break;
            case MessageType.GAMES_IN_PROGRESS:
                _gameService.gamesInProgress(session);
                break;
        }
    }
}
