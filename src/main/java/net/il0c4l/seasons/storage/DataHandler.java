package net.il0c4l.seasons.storage;

import net.il0c4l.seasons.Main;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class DataHandler {

    private Main plugin;
    private static final String[] DATA_TYPES = {"mysql", "yaml", "sqlite"};

    protected ArrayList<Entry> entries;

    public DataHandler(final Main plugin){
        this.plugin = plugin;
    }

    public static String getStorageType(FileConfiguration config){
        for(String str : config.getConfigurationSection("storage.type").getKeys(false)){
            if(config.getBoolean("storage.type." + str)){
                for(int i=0; i<DATA_TYPES.length; i++){
                    if(str.equalsIgnoreCase(DATA_TYPES[i])){
                        return str;
                    }
                }
            }
        }
        return "default";
    }

    public boolean entryExists(Entry entry) {
        return entries.contains(entry);
    }

    public boolean entryExists(UUID uuid){
        return entries.stream().anyMatch(match -> match.getUUID().equals(uuid));
    }

    public void addEntry(Entry entry){
        entries.add(entry);
    }

    public Entry getEntry(UUID uuid){
        return entries.stream().filter(match -> match.getUUID().equals(uuid)).findAny().orElse(null);
    }

    public void updateEntry(Entry entry){
        if(!entries.contains(entry)){
            return;
        }
        entries.set(entries.indexOf(entry), entry);
    }


    protected abstract ArrayList<Entry> getEntriesFromStorage();
    protected abstract CompletableFuture<ArrayList<Entry>> getEntriesFromStorageAsync();
    public abstract CompletableFuture<Boolean> containsUUIDAsync(UUID uuid);
    public abstract CompletableFuture<Void> syncOneEntryAsync(Entry entry);
    public abstract void syncEntries();

}
