package net.il0c4l.seasons.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;

public class SBlockIgniteEvent extends SBlockEvent {

    private String cause;

    public SBlockIgniteEvent(Player player, Block block, String cause){
        super(player, block);

    }
    public static boolean playerIgnited(BlockIgniteEvent e){
        for(BlockIgniteEvent.IgniteCause cause : BlockIgniteEvent.IgniteCause.values()){

        }
        return true;
    }
}
