package assignment.domain.events.game.start;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class StartEventPacket implements IEventPacket {
    public GameState game;

    public StartEventPacket(GameState game) {
        this.game = game;
    }
}