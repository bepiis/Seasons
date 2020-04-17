package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.Challenge;
import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.event.ChallengeCompletedEvent;
import net.il0c4l.seasons.event.SEntityDeathEvent;
import net.il0c4l.seasons.storage.DataHandler;
import net.il0c4l.seasons.storage.Entry;
import net.il0c4l.seasons.storage.SubEntry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener extends AbstractListener implements Listener {

    private ConfigHandler configHandler;
    private DataHandler storage;

    public EntityDeathListener(final Main plugin){
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        configHandler = plugin.getConfigHandler();
        storage = plugin.getDataHandler();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntity().getKiller() == null){
            return;
        }
        Material tool = e.getEntity().getKiller().getInventory().getItemInMainHand().getType();
        call(new SEntityDeathEvent(e.getEntity().getKiller(), e.getEntityType(), tool));
    }

    @EventHandler
    public void onEntityDeathChallengeEvent(SEntityDeathEvent e){
        String command = "EntityDeath.EntityType:" + e.getEntityType().name() + ".Material:" + e.getTool().toString();
        Challenge desired = configHandler.getDesiredChallenge(command);
        Entry entry = storage.getEntry(e.getPlayer().getUniqueId());

        SubEntry subEntry = entry.getSubEntry(command);

        if(desired == null || subEntry == null || subEntry.isDone() || !(desired.getCommand().equals(subEntry.getCommand()))){
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
