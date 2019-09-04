package assignment.domain.events.game.makeMove;

import assignment.infrastructure.events.IEventPacket;
import assignment.models.GameState;

public class MakeMoveEventPacket implements IEventPacket {
    public GameState game;

    public MakeMoveEventPacket(GameState game) {
        this.game = game;
    }
}