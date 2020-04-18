package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.event.ChallengeCompletedEvent;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class ChallengeListener extends AbstractListener implements Listener {

    private final Main plugin;

    public ChallengeListener(final Main plugin){
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onChallengeCompleted(ChallengeCompletedEvent e){

        plugin.sendMessage(e.getPlayer(), getChallengeCompletedMessage(e.getChallenge().getMessage()));

        ConfigHandler configHandler = plugin.getConfigHandler();
        Entry entry = e.getEntry();
        double points = entry.getPoints() + e.getChallenge().getPoints();
        e.getPlayer().sendMessage("new: "+points + "\nOld: " + entry.getPoints());

        if(points > configHandler.getTotalPoints()){
            points = configHandler.getTotalPoints();
            entry.setCompleted(true);
        }


        if(points - entry.getPoints() >= configHandler.getPointsPerTier()){
            List<Tier> completed = configHandler.getCompletedTiers((int)points, (int)entry.getPoints());
            sendToPlayer(e.getPlayer(), completed);
        }
        entry.setPoints(points);
    }
}
