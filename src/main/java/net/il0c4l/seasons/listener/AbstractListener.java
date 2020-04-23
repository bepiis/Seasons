package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.Challenge;
import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.event.PointChangeEvent;
import net.il0c4l.seasons.storage.DataHandler;
import net.il0c4l.seasons.storage.Entry;
import net.il0c4l.seasons.storage.SubEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import java.util.List;

public abstract class AbstractListener {

    private final Main plugin;
    private final ConfigHandler configHandler;
    private final DataHandler storage;

    public AbstractListener(final Main plugin){
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
        this.storage = plugin.getDataHandler();
    }

    public static void call(Main plugin, Event e){
        plugin.getServer().getPluginManager().callEvent(e);
    }

    //for inherited classes
    public void call(Event e){
        plugin.getServer().getPluginManager().callEvent(e);
    }

    public void sendToPlayer(Player p, List<Tier> completed){
        completed.forEach(it -> {
            it.getMessages().forEach(subItOne -> plugin.sendMessage(p,p, subItOne));
            it.getRewards().forEach(subItTwo -> subItTwo.giveReward(p));
        });
    }

    protected String getCompletedMessage(String msg){
        return plugin.getConfigHandler().getChallengeCompletedMessage().replace("{MESSAGE}", msg);
    }

    public void checkChallenge(Player p, String command){
        Challenge desired = configHandler.getDesiredChallenge(command);
        Entry entry = storage.getEntry(p.getUniqueId());

        SubEntry subEntry = entry.getSubEntry(command);

        if(subEntry == null || subEntry.isDone() || !(desired.getCommand().equals(subEntry.getCommand()))){
            return;
        }

        int progress = subEntry.getProgress();
        if(++progress == desired.getCount()){
            subEntry.setDone(true);
            plugin.sendMessage(p,p, getCompletedMessage(desired.getMessage()));
            double points = entry.getPoints() + desired.getPoints();

            if(points > configHandler.getTotalPoints()){
                points = configHandler.getTotalPoints();
                entry.setCompleted(true);
            }
            call(new PointChangeEvent(p.getUniqueId(), points, entry));
        }
        subEntry.setProgress(progress);
        entry.updateSubEntry(subEntry);
        storage.updateEntry(entry);
    }

}
