package assignment;

import assignment.models.*;
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
        Player player = new Player(session.getId(), message.sender);
        GameState game = new GameState();
        game.addPlayer(session.getId(), player);
        this.players.add(player);
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

    public void join(Session session, Message message) throws IOException, EncodeException {
        Request request = new Gson().fromJson(message.data, Request.class);
        Player player = new Player(session.getId(), message.sender);

        for (int i = 0; i < games.size(); i++) {
            GameState game = games.get(i);

            if (game.name.equals(request.gameName)) {
                game.addPlayer(session.getId(), player);
                this.players.add(player);

                Message reply = new Message();
                reply.type = MessageType.JOIN;
                reply.sender = "Server";

                for (Session user: users) {
                    Integer playerId = game.getPlayerId(user.getId());

                    if (playerId != null) {
                        reply.data = new Gson().toJson(new JoinResponse(true, game, playerId));
                        user.getBasicRemote().sendObject(reply);
                    }
                }
                break;
            }
        }


    }

    public void start(Session session, Message message) throws IOException, EncodeException {
        Request request = new Gson().fromJson(message.data, Request.class);

        for (int i = 0; i < games.size(); i++) {
            GameState game = games.get(i);
            if (game.name.equals(request.gameName)) {
                game.start();
                Message reply = new Message();
                reply.type = MessageType.START;
                reply.sender = "Server";
                reply.data = new Gson().toJson(new StartResponse(true, game));
                for (Session userSession: users) {
                    if (game.checkPlayerInGame(userSession.getId())) {
                        userSession.getBasicRemote().sendObject(reply);
                    }
                }

                break;
            }
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
        playerMoveRequest request = new Gson().fromJson(message.data, playerMoveRequest.class);

        playerLoop:
        for (int i = 0; i < this.players.size(); i++) {
            Player player = players.get(i);
            if (player.getSessionId().equals(session.getId())) {
                player.setNextMove(request.move);

                for (int j = 0; j < games.size(); j++) {
                    GameState game = games.get(j);

                    if (game.name.equals(request.gameName)) {
                        player.makeMove(game);
                        Message reply = new Message();
                        reply.type = MessageType.PLAYER_MOVE;
                        reply.sender = "Server";
                        reply.data = new Gson().toJson(new PlayerMoveResponse(true, game));
                        for (Session userSession: users) {
                            if (game.checkPlayerInGame(userSession.getId())) {
                                userSession.getBasicRemote().sendObject(reply);
                            }
                        }
                        break playerLoop;
                    }
                }
            }
        }
    }

    public void checkMove(Session session, Message message) throws IOException, EncodeException {
        checkMoveRequest request = new Gson().fromJson(message.data, checkMoveRequest.class);
        for (GameState game: games) {
            if (game.name.equals(request.gameName)) {
                boolean isAllowed = game.isMoveAllowed(request.move, request.playerId);
                Message reply = new Message();
                reply.type = MessageType.CHECK_MOVE;
                reply.sender = "Server";
                reply.data = new Gson().toJson(new checkMoveResponse(true, isAllowed, request.move));

                session.getBasicRemote().sendObject(reply);

                break;
            }
        }


    }
}
