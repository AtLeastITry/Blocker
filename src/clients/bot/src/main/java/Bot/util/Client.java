package Bot.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.java_websocket.handshake.ServerHandshake;

import Bot.ai.IAgent;
import Bot.models.Message;
import Bot.models.MessageType;

public class Client {
    private GameWebSocketClient _socket;

    public Client(String url) throws URISyntaxException {
        _socket = new GameWebSocketClient(new URI(url));
    }

    public <T> void Send(Message<T> message) {
        String request = JsonHelper.GSON.toJson(message);
        _socket.send(request);
    }

    public void open(IAgent agent) {
        // connect to the server
        _socket.connect();

        // add reference of the agent
        _socket.attach(agent);
    }

    public void close() {
        // close the connection
        _socket.close();

        // remove reference of the agent
        _socket.detach();
    }
}
