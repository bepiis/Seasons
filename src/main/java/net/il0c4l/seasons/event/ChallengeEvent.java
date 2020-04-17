package net.il0c4l.seasons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChallengeEvent extends Event {

    private Player player;
    private boolean cancelled;

    public ChallengeEvent(Player player){
        this.player = player;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){ return handlers; }

    private static final HandlerList handlers = new HandlerList();

    public void setCancelled(boolean cancel){
        cancelled = cancel;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public Player getPlayer(){
        return player;
    }
}
