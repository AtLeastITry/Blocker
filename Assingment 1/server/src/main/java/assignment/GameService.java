package assignment;

import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public class GameService {
    public ArrayList<Session> users = new ArrayList<Session>();
    public ArrayList<Player> players = new ArrayList<Player>();
    public ArrayList<GameState> games = new ArrayList<GameState>();

    public void host(Session session, Message message) throws IOException, EncodeException {
        this.users.add(session);
        Player player = new Player(1, session.getId(), message.sender);
        this.players.add(player);
        GameState game = new GameState();
        game.addPlayer(session.getId(), player.getMyPlayerId());
        this.games.add(game);
        Message reply = new Message();
        reply.type = MessageType.HOST;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new HostResponse(true, game, player.getMyPlayerId()));

        session.getBasicRemote().sendObject(reply);

        Message newGameResponse = new Message();
        newGameResponse.type = MessageType.NEW_GAME;
        newGameResponse.sender = "Server";
        newGameResponse.data = new Gson().toJson(new NewGameResponse(true, game.name));

        for (Session userSession: users) {
            userSession.getBasicRemote().sendObject(newGameResponse);
        }
    }

    public void allGames(Session session, Message message) throws IOException, EncodeException {
        List<String> gameNames = new ArrayList<>();

        for (GameState game: games) {
            gameNames.add(game.name);
        }

        Message reply = new Message();
        reply.type = MessageType.ALL_GAMES;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new AllGamesResponse(true, gameNames));

        for (Session userSession: users) {
            userSession.getBasicRemote().sendObject(reply);
        }
    }

    public void playerMove(Session session, Message message) throws IOException, EncodeException {
        Move move = new Gson().fromJson(message.data, Move.class);

        playerLoop:
        for (int i = 0; i < this.players.size(); i++) {
            Player player = players.get(i);
            if (player.getSessionId() == session.getId()) {
                player.setNextMove(move);
                for (int j = 0; j < games.size(); j++) {
                    GameState game = games.get(j);

                    if (game.getPlayerSessionIds().contains(player.getSessionId())) {
                        player.makeMove(game);
                        Message reply = new Message();
                        reply.type = MessageType.PLAYER_MOVE;
                        reply.sender = "Server";
                        reply.data = new Gson().toJson(new PlayerMoveResponse(true, game));

                        for (Session userSession: users) {
                            if (game.getPlayerSessionIds().contains(userSession.getId())) {
                                userSession.getBasicRemote().sendObject(reply);
                            }
                        }

                        break playerLoop;
                    }
                }
            }
        }
    }
}
