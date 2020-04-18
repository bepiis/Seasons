package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import java.util.List;


public abstract class AbstractListener {

    private final Main plugin;

    public AbstractListener(final Main plugin){
        this.plugin = plugin;
    }

    public void call(Event event){
        plugin.getServer().getPluginManager().callEvent(event);
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
