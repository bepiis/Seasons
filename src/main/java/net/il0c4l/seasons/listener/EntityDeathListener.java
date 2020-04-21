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

    private final ConfigHandler configHandler;
    private final DataHandler storage;

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
        e.getEntity().getKiller().sendMessage("test");
        Material tool = e.getEntity().getKiller().getInventory().getItemInMainHand().getType();
        call(new SEntityDeathEvent(e.getEntity().getKiller(), e.getEntityType(), tool));
    }

    @EventHandler
    public void onEntityDeathChallengeEvent(SEntityDeathEvent e){
        String command = "EntityDeath.EntityType:" + e.getEntityType().name() + ".Material:" + e.getTool().toString();
        checkChallenge(e, command);
    }
}
