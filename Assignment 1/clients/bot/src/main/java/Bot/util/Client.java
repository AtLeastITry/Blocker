package Bot.util;

import org.java_websocket.client.WebSocketClient;

import Bot.models.Message;
import Bot.util.JsonHelper;

import java.net.URI;
import java.net.URISyntaxException;

public class Client {
    private WebSocketClient _socket;

    public Client(String url) throws URISyntaxException {
        _socket = new GameWebSocketClient(new URI(url));
    }

    public <T> void Send(Message<T> message) {
        _socket.send(JsonHelper.GSON.toJson(message));
    }

    public void open() {
        if (_socket.isClosed()) {
            _socket.connect();
        }
    }

    public void close() {
        if (_socket.isOpen()) {
            _socket.close();
        }
    }
}
