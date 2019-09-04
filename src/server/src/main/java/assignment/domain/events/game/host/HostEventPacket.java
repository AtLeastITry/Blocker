package assignment.domain.events.game.host;

import javax.websocket.Session;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class HostEventPacket implements IEventPacket {
    public GameState game;
    public Integer playerId;
    public Session currentUser;

    public HostEventPacket(GameState game, Integer playerId, Session currentUser) {
        this.game = game;
        this.playerId = playerId;
        this.currentUser = currentUser;
    }
}