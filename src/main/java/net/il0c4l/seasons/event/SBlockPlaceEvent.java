package net.il0c4l.seasons.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SBlockPlaceEvent extends SBlockEvent {

    private Block blockAgainst;

    public SBlockPlaceEvent(Player player, Block block, Block blockAgainst){
        super(player, block);
        this.blockAgainst = blockAgainst;
    }

    public Block getBlockAgainst(){
        return blockAgainst;
    }

    public String getBlockAgainstToString(){
        return blockAgainst.toString();
    }
}
