package assignment.domain.events.game.spectate;

import javax.websocket.Session;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class SpectateEventPacket implements IEventPacket {
    public GameState game;
    public Session currentUser;

    public SpectateEventPacket(GameState game, Session currentUser) {
        this.game = game;
        this.currentUser = currentUser;
    }
}