package net.il0c4l.seasons.event;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public final class SEntityDeathEvent extends ChallengeEvent {

    private EntityType entityType;
    private Material tool;
    private Player player;
    private boolean cancelled;

    public SEntityDeathEvent(Player player, EntityType entityType, Material tool){
        super(player);
        this.entityType = entityType;
        this.tool = tool;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public EntityType getEntityType(){
        return entityType;
    }

    public Material getTool(){
        return tool;
    }

    public static final HandlerList handlers = new HandlerList();

    public void setCancelled(boolean cancel){
        cancelled = cancel;
    }

    public boolean isCancelled(){
        return cancelled;
    }
}
