package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.event.SBlockBreakEvent;
import net.il0c4l.seasons.event.SBlockPlaceEvent;
import net.il0c4l.seasons.storage.DataHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlocksListener extends AbstractListener implements Listener{

    private final ConfigHandler challengeHandler;
    private final DataHandler storage;

    public BlocksListener(final Main plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        challengeHandler = plugin.getConfigHandler();
        storage = plugin.getDataHandler();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        call(new SBlockBreakEvent(e.getPlayer(), e.getBlock(), e.getPlayer().getInventory().getItemInMainHand().getType(), e.isDropItems()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(!e.canBuild()){
            return;
        }
        call(new SBlockPlaceEvent(e.getPlayer(), e.getBlockPlaced(), e.getBlockAgainst()));
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e){
        if(!(e.getIgnitingEntity() instanceof Player)) {
            return;
        }
    }
}
