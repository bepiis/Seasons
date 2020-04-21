package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.Challenge;
import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.event.ChallengeCompletedEvent;
import net.il0c4l.seasons.event.ChallengeEvent;
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

    public void checkChallenge(ChallengeEvent e, String command){
        Challenge desired = configHandler.getDesiredChallenge(command);
        Entry entry = storage.getEntry(e.getPlayer().getUniqueId());

        SubEntry subEntry = entry.getSubEntry(command);

        if(subEntry == null || subEntry.isDone() || !(desired.getCommand().equals(subEntry.getCommand()))){
            return;
        }

        int progress = subEntry.getProgress();
        if(++progress == desired.getCount()){
            subEntry.setDone(true);
            call(new ChallengeCompletedEvent(e.getPlayer(), entry, desired));
        }
        subEntry.setProgress(progress);
        entry.updateSubEntry(subEntry);
        storage.updateEntry(entry);
    }

}
