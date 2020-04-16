package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.Utils;
import net.il0c4l.seasons.event.ChallengeCompletedEvent;
import net.il0c4l.seasons.reward.Reward;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractListener implements Listener {

    private Main plugin;

    public AbstractListener(final Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void call(Event event){
        plugin.getServer().getPluginManager().callEvent(event);
    }

    @EventHandler
    public void onChallengeCompleted(ChallengeCompletedEvent e){
        ConfigHandler configHandler = plugin.getConfigHandler();
        Entry entry = plugin.getDataHandler().getEntry(e.getPlayer().getUniqueId());
        int points = entry.getPoints() + e.getEarned();

        if(points > configHandler.getTotalPoints()){
            points = (int)configHandler.getTotalPoints();
            entry.setCompleted(true);
        }

        if(points - entry.getPoints() >= configHandler.getPointsPerTier()){
            List<Tier> completed = configHandler.getCompletedTiers(points, entry.getPoints());
        }

        entry.setPoints(points);
    }

    public void giveRewards(Player player, List<Tier> completed){
        List<Reward> rewards = new ArrayList<>();

    }

}
