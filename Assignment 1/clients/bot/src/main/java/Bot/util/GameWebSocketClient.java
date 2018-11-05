package Bot.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import Bot.ai.IAgent;
import Bot.models.*;
import Bot.models.Responses.*;

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
        String request = JsonHelper.GSON.toJson(new Message<String>(MessageType.ALL_GAMES, "client", ""));
        send(request);
    }

    @Override
    public void onMessage(String s) {
        if (_agent != null) {
            Event event = JsonHelper.GSON.fromJson(s, Event.class);

            switch(event.type) {
                case MessageType.PLAYER_MOVE:
                    PlayerMoveResponse playerMoveResponse = JsonHelper.GSON.fromJson(event.data, PlayerMoveResponse.class);
                    _agent.playerMoveAction(playerMoveResponse);
                    break;
                case MessageType.HOST:
                    HostResponse hostResponse = JsonHelper.GSON.fromJson(event.data, HostResponse.class);
                    _agent.hostAction(hostResponse);
                    break;
                case MessageType.ALL_GAMES:
                    AllGamesResponse allGamesResponse = JsonHelper.GSON.fromJson(event.data, AllGamesResponse.class);
                    _agent.allGamesAction(allGamesResponse);
                    break;
                case MessageType.NEW_GAME:
                    NewGameResponse newGameResponse = JsonHelper.GSON.fromJson(event.data, NewGameResponse.class);
                    _agent.newGameAction(newGameResponse);
                    break;
                case MessageType.START:
                    StartResponse startResponse = JsonHelper.GSON.fromJson(event.data, StartResponse.class);
                    _agent.startAction(startResponse);
                    break;
                case MessageType.JOIN:
                    JoinResponse joinResponse = JsonHelper.GSON.fromJson(event.data, JoinResponse.class);
                    _agent.joinAction(joinResponse);
                    break;
                case MessageType.CHECK_MULTIPLE_MOVES:
                    CheckMultipleMovesResponse checkMultipleMovesResponse = JsonHelper.GSON.fromJson(event.data, CheckMultipleMovesResponse.class);
                    _agent.checkMultipleMoveAction(checkMultipleMovesResponse);
                    break;
                case MessageType.LEAVE:
                    LeaveResponse leaveResponse = JsonHelper.GSON.fromJson(event.data, LeaveResponse.class);
                    _agent.leaveAction(leaveResponse);
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
