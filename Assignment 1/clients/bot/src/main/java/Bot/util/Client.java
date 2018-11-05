package Bot.util;

import java.net.URI;
import java.net.URISyntaxException;

import Bot.ai.IAgent;
import Bot.models.Message;
import Bot.models.MessageType;

public class Client {
    private GameWebSocketClient _socket;

    public Client(String url) throws URISyntaxException {
        _socket = new GameWebSocketClient(new URI(url));
    }

    public <T> void Send(Message<T> message) {
        _socket.send(JsonHelper.GSON.toJson(message));
    }

    public void open(IAgent agent) {
        // check if the socket is closed
        if (_socket.isClosed()) {
            // connect to the server
            _socket.connect();

            // add reference of the agent
            _socket.attach(agent);

            // ask server for initial information
            this.Send(new Message<String>(MessageType.INIT, "client", ""));
        }
    }

    public void close() {
        // check if the socket is open
        if (_socket.isOpen()) {
            // close the connection
            _socket.close();

            // remove reference of the agent
            _socket.detach();
        }
    }
}
