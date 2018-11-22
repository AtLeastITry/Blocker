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
        Player player = new Player(session.getId());
        GameState game = new GameState("Game " + (games.size() + 1));
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
        Player player = new Player(session.getId());

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

        this.allGames();
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

                    Message gameInProgressReply = new Message();
                    gameInProgressReply.type = MessageType.NEW_GAME_IN_PROGRESS;
                    gameInProgressReply.sender = "Server";
                    gameInProgressReply.data = new Gson().toJson(new NewGameInProgressResponse(true, new Game(game.name, game.getNumberOfPlayers())));

                    userSession.getBasicRemote().sendObject(gameInProgressReply);
                }

                break;
            }
        }

        this.allGames();
    }

    public void leave(Session session, Message message) throws IOException, EncodeException {
        LeaveRequest request = new Gson().fromJson(message.data, LeaveRequest.class);
        Integer gameIndex = null;

        for (int i = 0; i < games.size(); i++) {
            GameState game = games.get(i);
            if (game.name.equals(request.gameName)) {
                gameIndex = i;
                game.removePlayer(request.playerId);
                Message reply = new Message();
                reply.type = MessageType.LEAVE;
                reply.sender = "Server";
                reply.data = new Gson().toJson(new LeaveResponse(true, game, false));
                if (game.getNumberOfPlayers() > 0) {
                    for (Session userSession: users) {
                        if (game.checkPlayerInGame(userSession.getId())) {
                            userSession.getBasicRemote().sendObject(reply);
                        }
                    }
                }

                reply.data = new Gson().toJson(new LeaveResponse(true, game, true));
                session.getBasicRemote().sendObject(reply);
                break;
            }
        }

        if (gameIndex != null) {
            games.remove(gameIndex.intValue());
        }

        this.allGames();
    }

    public void allGames() throws IOException, EncodeException {
        List<String> gameNames = new ArrayList<>();

        for (GameState game: games) {
            if (!game.getInPogress() && game.getNumberOfPlayers() < 5) {
                gameNames.add(game.name);
            }
        }

        Message reply = new Message();
        reply.type = MessageType.ALL_GAMES;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new AllGamesResponse(true, gameNames));

        for (Session userSession: users) {
            userSession.getBasicRemote().sendObject(reply);
        }
    }

    public void spectate(Session session, Message message) throws IOException, EncodeException {
        Request request = new Gson().fromJson(message.data, Request.class);

        for (int i = 0; i < games.size(); i++) {
            GameState game = games.get(i);
            if (game.name.equals(request.gameName)) {
                game.addSpectator(session.getId());

                Message reply = new Message();
                reply.type = MessageType.SPECTATE_GAME;
                reply.sender = "Server";
                reply.data = new Gson().toJson(new SpectateResponse(true, game));

                session.getBasicRemote().sendObject(reply);
            }
        }
    }

    public void gamesInProgress(Session session) throws IOException, EncodeException {
        List<Game> gamesInprogress = new ArrayList<>();

        for (GameState game: games) {
            if (game.getInPogress()) {
                gamesInprogress.add(new Game(game.name, game.getNumberOfPlayers()));
            }
        }

        Message reply = new Message();
        reply.type = MessageType.GAMES_IN_PROGRESS;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new GamesInProgressResponse(true, gamesInprogress));

        session.getBasicRemote().sendObject(reply);
    }

    public void playerMove(Session session, Message message) throws IOException, EncodeException {
        PlayerMoveRequest request = new Gson().fromJson(message.data, PlayerMoveRequest.class);

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
        CheckMoveRequest request = new Gson().fromJson(message.data, CheckMoveRequest.class);
        for (GameState game: games) {
            if (game.name.equals(request.gameName)) {
                boolean isAllowed = game.isMoveAllowed(request.move, request.playerId);
                Message reply = new Message();
                reply.type = MessageType.CHECK_MOVE;
                reply.sender = "Server";
                reply.data = new Gson().toJson(new CheckMoveResponse(true, isAllowed, request.move));

                session.getBasicRemote().sendObject(reply);

                break;
            }
        }


    }

    public void checkMultipleMoves(Session session, Message message) throws IOException, EncodeException {
        CheckMultipleMovesRequest request = new Gson().fromJson(message.data, CheckMultipleMovesRequest.class);
        for (GameState game: games) {
            if (game.name.equals(request.gameName)) {
                ArrayList<MoveResponse> moves = new ArrayList<>();
                
                for(Move move: request.moves) {
                    boolean isAllowed = game.isMoveAllowed(move, request.playerId);
                    moves.add(new MoveResponse(move, isAllowed));
                }

                ArrayList<MoveResponse> brokenMoves = new ArrayList<>();

                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i).allowed) {
                        int x = moves.get(i).move.getFirstMove().getX();
                        int y = moves.get(i).move.getFirstMove().getY();

                        if (x < 0 || x > 5 || y < 0 || y > 9) {
                            brokenMoves.add(moves.get(i));
                        }                        
                    }
                }

                for (MoveResponse move : brokenMoves) {
                    game.isMoveAllowed(move.move, request.playerId);
                }
                
                Message reply = new Message();
                reply.type = MessageType.CHECK_MULTIPLE_MOVES;
                reply.sender = "Server";
                reply.data = new Gson().toJson(new CheckMultipleMovesResponse(true, moves));

                session.getBasicRemote().sendObject(reply);

                break;
            }
        }


    }
}
