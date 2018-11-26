package assignment.domain.events.game.join;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class JoinEventPacket implements IEventPacket {
    public GameState game;

    public JoinEventPacket(GameState game) {
        this.game = game;
    }
}