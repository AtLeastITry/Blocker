package assignment.domain.events.game.leave;

import javax.websocket.Session;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class LeaveEventPacket implements IEventPacket {
    public GameState game;
    public Session currentUser; 

    public LeaveEventPacket(GameState game, Session currentUser) {
        this.game = game;
        this.currentUser = currentUser;
    }
}