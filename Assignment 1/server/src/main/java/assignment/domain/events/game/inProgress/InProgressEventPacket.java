package assignment.domain.events.game.inProgress;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class InProgressEventPacket implements IEventPacket {
    public GameState game;

    public InProgressEventPacket(GameState game) {
        this.game = game;
    }
}