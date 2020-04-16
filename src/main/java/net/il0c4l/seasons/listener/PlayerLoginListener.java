package net.il0c4l.seasons.listener;

import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.storage.DataHandler;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PlayerLoginListener extends AbstractListener implements Listener {

    private Main plugin;
    private DataHandler storage;

    public PlayerLoginListener(Main plugin, DataHandler storage){
        super(plugin);
        this.plugin = plugin;
        this.storage = storage;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(!plugin.getDataHandler().entryExists(e.getPlayer().getUniqueId())){
            plugin.getDataHandler().addEntry(new Entry(e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        try{
            UUID uuid = e.getPlayer().getUniqueId();
            storage.syncOneEntryAsync(storage.getEntry(uuid)).get();
            storage.removeEntry(storage.getEntry(uuid));
        } catch(ExecutionException | InterruptedException ex){
            ex.printStackTrace();
        }
    }

}
