package Bot.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import Bot.ai.IAgent;
import Bot.models.Event;
import Bot.models.MessageType;
import Bot.models.Responses.HostResponse;
import Bot.util.JsonHelper;

import java.net.URI;

public class GameWebSocketClient extends WebSocketClient {
    private IAgent _agent;

    public GameWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public void attach(IAgent agent) {
        _agent = agent;
    }

    public void detach() {
        _agent = null;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        if (_agent != null) {
            Event event = JsonHelper.GSON.fromJson(s, Event.class);

            switch(event.type) {
                case MessageType.PLAYER_MOVE:
                case MessageType.HOST:
                    HostResponse response = JsonHelper.GSON.fromJson(event.data, HostResponse.class);
                    _agent.updateGame(response.game);
                    _agent.updatePlayer(response.player);
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
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
