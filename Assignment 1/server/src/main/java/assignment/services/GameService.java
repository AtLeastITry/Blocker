package assignment.services;

import assignment.models.*;
import assignment.models.requests.*;
import assignment.models.responses.*;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

        this.users.stream().forEach(user -> {
            try {
                user.getBasicRemote().sendObject(newGameResponse);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    public void join(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        Player player = new Player(session.getId());

        Message reply = new Message();
        reply.type = MessageType.JOIN;
        reply.sender = "Server";

        this.games.stream()
                  .filter(game -> game.name.equals(request.gameName))
                  .forEach(game -> {
                      game.addPlayer(session.getId(), player);
                          this.players.add(player);
      
                          this.users.stream()
                                    .map(u -> new Object() {
                                        Session session = u;
                                        Integer playerId = game.getPlayerId(u.getId());
                                    })
                                    .forEach(user -> {
                                        if (user.playerId != null) {
                                            reply.data = new Gson().toJson(new JoinResponse(true, game, user.playerId));
                                                    try {
                                                        user.session.getBasicRemote().sendObject(reply);
                                                    } catch (IOException | EncodeException e) {
                                                        e.printStackTrace();
                                                    }
                                        }
                                    });
                  });
      
        this.allGames();
    }

    public void start(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        Message reply = new Message();
        reply.type = MessageType.START;
        reply.sender = "Server";

        Message gameInProgressReply = new Message();
        gameInProgressReply.type = MessageType.NEW_GAME_IN_PROGRESS;
        gameInProgressReply.sender = "Server";
        
        this.games.stream()
                  .filter(game -> game.name.equals(request.gameName))
                  .forEach(game -> {
                      game.start();
                      reply.data = new Gson().toJson(new StartResponse(true, game));
      
                      this.users.stream()
                                .forEach(user -> {
                                    if (game.checkPlayerInGame(user.getId())) {
                                        try {
                                            user.getBasicRemote().sendObject(reply);
                                        } catch (IOException | EncodeException e) {
                                            e.printStackTrace();
                                        }
                                    }
        
                                    gameInProgressReply.data = new Gson().toJson(new NewGameInProgressResponse(true, new Game(game.name, game.getNumberOfPlayers())));
                                    try {
                                        user.getBasicRemote().sendObject(gameInProgressReply);
                                    } catch (IOException | EncodeException e) {
                                        e.printStackTrace();
                                    }
                                });
                  }); 

        this.allGames();
    }

    public void leave(Session session, Message message) {
        LeaveRequest request = new Gson().fromJson(message.data, LeaveRequest.class);
        Message reply = new Message();
        reply.type = MessageType.LEAVE;
        reply.sender = "Server";

        this.games.stream()
                  .filter(game -> game.name.equals(request.gameName))
                  .forEach(game -> {
                      game.removePlayer(request.playerId);
                      reply.data = new Gson().toJson(new LeaveResponse(true, game, false));
          
                      if (game.getNumberOfPlayers() > 0) {
                          this.users.stream()
                                    .filter(user -> game.checkPlayerInGame(user.getId()))
                                    .forEach(user -> {
                                        try {
                                            user.getBasicRemote().sendObject(reply);
                                        } catch (IOException | EncodeException e) {
                                            e.printStackTrace();
                                        }
                                    });
                      }
                      
                      reply.data = new Gson().toJson(new LeaveResponse(true, game, true));
                      try {
                          session.getBasicRemote().sendObject(reply);
                      } catch (IOException | EncodeException e) {
                          e.printStackTrace();
                      }
                  });

        games.removeIf(game -> game.getNumberOfPlayers() == 0 && game.name.equals(request.gameName));

        this.allGames();
    }

    public void allGames() {
        List<String> gameNames = this.games.stream()
                                           .filter(game -> !game.getInPogress() && game.getNumberOfPlayers() < 5)
                                           .map(game -> game.name).collect(Collectors.toList());

        Message reply = new Message();
        reply.type = MessageType.ALL_GAMES;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new AllGamesResponse(true, gameNames));

        this.users.stream().forEach(user -> {
            try {
                user.getBasicRemote().sendObject(reply);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    public void spectate(Session session, Message message) {
        Request request = new Gson().fromJson(message.data, Request.class);
        Message reply = new Message();
        reply.type = MessageType.SPECTATE_GAME;
        reply.sender = "Server";

        this.games.stream()
                  .filter(game -> game.name.equals(request.gameName))
                  .forEach(game -> {
                      game.addSpectator(session.getId());
                      reply.data = new Gson().toJson(new SpectateResponse(true, game));
                      try {
                          session.getBasicRemote().sendObject(reply);
                      } catch (IOException | EncodeException e) {
                          e.printStackTrace();
                      }
                  });
    }

    public void gamesInProgress(Session session) throws IOException, EncodeException {
        List<Game> gamesInprogress = this.games.stream()
                                               .filter(game -> game.getInPogress())
                                               .map(game -> new Game(game.name, game.getNumberOfPlayers()))
                                               .collect(Collectors.toList());

        Message reply = new Message();
        reply.type = MessageType.GAMES_IN_PROGRESS;
        reply.sender = "Server";
        reply.data = new Gson().toJson(new GamesInProgressResponse(true, gamesInprogress));

        session.getBasicRemote().sendObject(reply);
    }

    public void playerMove(Session session, Message message) throws IOException, EncodeException {
        PlayerMoveRequest request = new Gson().fromJson(message.data, PlayerMoveRequest.class);

        Message reply = new Message();
        reply.type = MessageType.PLAYER_MOVE;
        reply.sender = "Server";

        this.games.stream()
                  .filter(game -> game.name.equals(request.gameName))
                  .forEach(game -> {
                      this.players.stream()
                                  .filter(player -> player.getSessionId().equals(session.getId()))
                                  .forEach(player -> {
                                      player.setNextMove(request.move);
                                      player.makeMove(game);
                                  });
                                  
                      reply.data = new Gson().toJson(new PlayerMoveResponse(true, game));
          
                      this.users.stream()
                                .filter(user -> game.checkPlayerInGame(user.getId()))
                                .forEach(user -> {
                                    try {
                                        user.getBasicRemote().sendObject(reply);
                                    } catch (IOException | EncodeException e) {
                                        e.printStackTrace();
                                    }
                                });
                  });
    }

    public void checkMove(Session session, Message message) {
        CheckMoveRequest request = new Gson().fromJson(message.data, CheckMoveRequest.class);

        Message reply = new Message();
        reply.type = MessageType.CHECK_MOVE;
        reply.sender = "Server";

        this.games.stream()
                  .filter(game -> game.name.equals(request.gameName))
                  .forEach(game -> {
                      boolean isAllowed = game.isMoveAllowed(request.move, request.playerId);
                      reply.data = new Gson().toJson(new CheckMoveResponse(true, isAllowed, request.move));
          
                      try {
                          session.getBasicRemote().sendObject(reply);
                      } catch (IOException | EncodeException e) {
                          e.printStackTrace();
                      }
                  });
    }

    public void checkMultipleMoves(Session session, Message message) {
        CheckMultipleMovesRequest request = new Gson().fromJson(message.data, CheckMultipleMovesRequest.class);

        Message reply = new Message();
        reply.type = MessageType.CHECK_MULTIPLE_MOVES;
        reply.sender = "Server";

        this.games.stream().filter(game -> game.name.equals(request.gameName))
        .forEach(game -> {
            List<MoveResponse> moves = request.moves.stream()
                                                    .map(move -> new MoveResponse(move, game.isMoveAllowed(move, request.playerId)))
                                                    .collect(Collectors.toList());

            reply.data = new Gson().toJson(new CheckMultipleMovesResponse(true, moves));

            try {
                session.getBasicRemote().sendObject(reply);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }
}
