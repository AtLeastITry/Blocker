package assignment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Root resource (exposed at "game" path)
 */
@ServerEndpoint(value = "/game", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class GameController {

    public static Set<Session> Users = Collections.synchronizedSet(new HashSet<Session>());
    public static Set<Player> Players = Collections.synchronizedSet(new HashSet<Player>());
    public static Set<GameState> Games = Collections.synchronizedSet(new HashSet<GameState>());

    @OnOpen
    public void open(Session session) {
        Users.add(session);
    }

    @OnClose
    public void close(final Session session) {
        Players.removeIf(player -> player.getSessionId() == session.getId());
        for (GameState gameState: Games) {
            gameState.removePlayer(session.getId());
        }
        Users.remove(session);
    }

    @OnError
    public void onError(Throwable error) {
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
    }
}
