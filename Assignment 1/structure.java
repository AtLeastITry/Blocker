import javax.websocket.*;

public class Idea {
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    static Set<Game> games = Collections.synchronizedSet(new HashSet<Game>());

    @OnOpen
    public void onOpen(Session session) {
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        //broadcast the message
        if (message.type == gameMove) {
            // get game with gameid
            // go through each player in game and send message to player
        }

        if (join) {
            // add player to game
        }

        if (host) {
            //create new game and add player
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        //remove player from peers and from game
    }
}

public class Game {
    public String Id;
    public HashSet<string> playerIds;
}