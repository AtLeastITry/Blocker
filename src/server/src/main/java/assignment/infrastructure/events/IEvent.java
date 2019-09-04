package assignment.infrastructure.events;

import java.util.concurrent.CompletableFuture;

public interface IEvent<TPacket extends IEventPacket> {
    void fire(TPacket packet);
}