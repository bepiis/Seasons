package net.il0c4l.seasons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class ChallengeEvent extends Event {

    private UUID uuid;
    private boolean cancelled;

    public ChallengeEvent(UUID uuid){
        this.uuid = uuid;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){ return handlers; }

    protected static final HandlerList handlers = new HandlerList();

    public void setCancelled(boolean cancel){
        cancelled = cancel;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public UUID getUUID(){
        return uuid;
    }
}
