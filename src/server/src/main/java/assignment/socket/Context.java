package assignment.socket;

import java.util.ArrayList;

import javax.websocket.Session;

import assignment.models.GameState;
import assignment.models.Player;

public class Context {
    public final ArrayList<Session> users = new ArrayList<Session>();
    public final ArrayList<Player> players = new ArrayList<Player>();
    public final ArrayList<GameState> games = new ArrayList<GameState>();
}