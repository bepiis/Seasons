package net.il0c4l.seasons.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class SBlockEvent extends ChallengeEvent {

    private Player player;
    private Block block;

    public SBlockEvent(Player player, Block block){
        super(player);
        this.player = player;
        this.block = block;
    }

    public Player getPlayer(){ return player; }

    public Block getBlock(){
        return block;
    }
}
