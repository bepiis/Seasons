package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.event.ChallengeSetEvent;
import net.il0c4l.seasons.event.PointChangeEvent;
import net.il0c4l.seasons.storage.Entry;
import net.il0c4l.seasons.storage.SubEntry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.List;

public class EntryListener extends AbstractListener implements Listener {

    private final Main plugin;

    public EntryListener(final Main plugin){
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPointChange(PointChangeEvent e){
        ConfigHandler configHandler = plugin.getConfigHandler();

        double points = e.getPoints();

        if(points > e.getPastPoints()){
            if(points > configHandler.getTotalPoints()){
                points = configHandler.getTotalPoints();
                e.getEntry().setCompleted(true);
            }
            List<Tier> completed = configHandler.getCompletedTiers((int)points, (int)e.getPastPoints());
            if(!completed.isEmpty()){
                sendToPlayer(Bukkit.getPlayer(e.getUUID()), completed);
            }
        }
        e.getEntry().setPoints(points);
    }

    @EventHandler
    public void onChallengeSet(ChallengeSetEvent e){
        Entry entry = e.getEntry();
        String msg = "Challenge set for {PLAYER}";
        if(entry.getSubEntry(e.getCommand()) == null) {
            entry.addSubEntry(new SubEntry(e.getUUID(), e.getCommand(), e.getProgress(), false));
        } else{
            msg = "Challenge is already set for {PLAYER}!";
        }

        if(e.setFromCommand()){
            plugin.sendMessage(e.getSender(), e.getUUID(), msg);
        }
    }

}
