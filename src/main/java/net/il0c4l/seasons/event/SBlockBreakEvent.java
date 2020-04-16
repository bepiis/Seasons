package net.il0c4l.seasons.event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SBlockBreakEvent extends SBlockEvent {

    private Material tool;
    private boolean drops;

    public SBlockBreakEvent(Player player, Block block, Material tool, boolean drops){
        super(player, block);
        this.tool = tool;
        this.drops = drops;
    }

    public Material getTool(){
        return tool;
    }

    public String getToolToString(){
        return tool.toString();
    }

    public boolean isDrops(){
        return drops;
    }
}
