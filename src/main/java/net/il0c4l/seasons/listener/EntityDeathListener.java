package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener extends AbstractListener implements Listener {

    public EntityDeathListener(final Main plugin){
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntity().getKiller() == null){
            return;
        }
        Material tool = e.getEntity().getKiller().getInventory().getItemInMainHand().getType();

        String command = "EntityDeath.EntityType:" + e.getEntityType().name() + ".Material:" + tool.toString();
        checkChallenge(e.getEntity().getKiller(), command);
    }

}
