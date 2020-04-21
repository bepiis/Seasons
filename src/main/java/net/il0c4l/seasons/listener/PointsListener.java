package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.event.ChallengeCompletedEvent;
import net.il0c4l.seasons.event.PointChangeEvent;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class PointsListener extends AbstractListener implements Listener {

    private final Main plugin;

    public PointsListener(final Main plugin){
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPointChange(PointChangeEvent e){
        ConfigHandler configHandler = plugin.getConfigHandler();

        List<Tier> completed = configHandler.getCompletedTiers((int)e.getPoints(), (int)e.getPastPoints());
        if(!completed.isEmpty()){
            sendToPlayer(e.getPlayer(), completed);
        }
        e.getEntry().setPoints(e.getPoints());
    }
}
