package net.il0c4l.seasons.listener;

import de.themoep.inventorygui.GuiStateElement;
import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.gui.GUI;
import net.il0c4l.seasons.storage.DataHandler;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class PlayerLoginListener extends AbstractListener implements Listener {

    private final DataHandler storage;
    private final Main plugin;

    public PlayerLoginListener(final Main plugin){
        super(plugin);
        this.plugin = plugin;
        storage = plugin.getDataHandler();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(!storage.entryExists(e.getPlayer().getUniqueId())){
            storage.addEntry(new Entry(e.getPlayer().getUniqueId()));
        }

        plugin.getGui().updateProgBar(storage.getEntry(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        try{
            UUID uuid = e.getPlayer().getUniqueId();
            storage.syncOneEntryAsync(storage.getEntry(uuid)).get();
        } catch(ExecutionException | InterruptedException ex){
            ex.printStackTrace();
        }
    }

}
