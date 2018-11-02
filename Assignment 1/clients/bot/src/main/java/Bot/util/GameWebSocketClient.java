package Bot.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import Bot.models.Event;
import Bot.models.MessageType;
import Bot.util.JsonHelper;

import java.net.URI;

public class GameWebSocketClient extends WebSocketClient {
    public GameWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        Event event = JsonHelper.GSON.fromJson(s, Event.class);

        switch(event.type) {
            case MessageType.HOST:
                break;
            case MessageType.PLAYER_MOVE:
                break;
            case MessageType.ALL_GAMES:
                break;
            case MessageType.NEW_GAME:
                break;
            case MessageType.START:
                break;
            case MessageType.JOIN:
                break;
            case MessageType.CHECK_MOVE:
                break;
            case MessageType.LEAVE:
                break;
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
