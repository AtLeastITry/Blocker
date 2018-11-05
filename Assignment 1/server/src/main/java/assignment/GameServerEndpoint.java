package assignment;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


@ServerEndpoint(value = "/game", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class GameServerEndpoint {

    private static GameService _gameService = new GameService();

    @OnOpen
    public void open(Session session) {
        _gameService.users.add(session);
    }

    @OnClose
    public void close(final Session session) {
        Player temp = null;

        for (Player player: _gameService.players) {
            if (player.getSessionId() == session.getId()) {
                temp = player;
            }
        }

        _gameService.players.removeIf(player -> player.getSessionId() == session.getId());
        for (GameState gameState: _gameService.games) {
            gameState.removePlayer(temp.getMyPlayerId());
        }
        _gameService.users.remove(session);
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
        }
    }
}
