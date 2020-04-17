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

        plugin.sendMessage(e.getPlayer(), getChallengeCompletedMessage(e.getChallenge().getMessage()));

        ConfigHandler configHandler = plugin.getConfigHandler();
        Entry entry = e.getEntry();
        double points = entry.getPoints() + e.getChallenge().getPoints();

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

    public void sendToPlayer(Player player, List<Tier> completed){
        completed.forEach(iter -> {
            iter.getMessages().forEach(subIterOne -> plugin.sendMessage(player, subIterOne));
            iter.getRewards().forEach(subIterTwo -> subIterTwo.giveReward(player));
        });
    }

    protected String getChallengeCompletedMessage(String msg){
        return plugin.getConfigHandler().getChallengeCompletedMessage().replace("{MESSAGE}", msg);
    }

}
