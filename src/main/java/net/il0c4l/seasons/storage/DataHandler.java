package net.il0c4l.seasons.storage;

import net.il0c4l.seasons.Main;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

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
        for(Entry entry : entries){
            if(entry.getUUID().equals(uuid)){
                return true;
            }
        }
        return false;
    }

    public void removeEntry(Entry entry){
        entries.remove(entry);
    }

    public void addEntry(Entry entry){
        entries.add(entry);
    }

    public Entry getEntry(UUID uuid){
        if(indexOfEntry(uuid) == -1){
            return null;
        }
        return entries.get(indexOfEntry(uuid));
    }

    protected int indexOfEntry(UUID uuid){
        int found = -1;
        for(int i=0; i<entries.size(); i++){
            if(entries.get(i).getUUID().equals(uuid)){
                found = i;
            }
        }
        return found;
    }

    protected int indexOfEntry(Entry entry){
        int found = -1;
        for(int i=0; i<entries.size(); i++){
            if(entries.get(i).equals(entry)){
                found = i;
            }
        }
        return found;
    }

    public void updateEntry(Entry entry){
        entries.forEach(iter -> {
            if(iter.equals(entry)){
                iter = entry;
            }
        });
    }

    public ArrayList<Entry> getEntries(){
        return entries;
    }

    public void addSubEntry(Entry entry, SubEntry subEntry){
        entry.addSubEntry(subEntry);
    }

    public void removeSubEntry(Entry entry, SubEntry subEntry){
        entry.removeSubEntry(subEntry);
    }

    public boolean subEntryExists(Entry entry, SubEntry subEntry){
        return entry.indexOfSubEntry(subEntry) != -1;
    }



    protected abstract ArrayList<Entry> getEntriesFromStorage();
    protected abstract CompletableFuture<ArrayList<Entry>> getEntriesFromStorageAsync();
    public abstract CompletableFuture<Boolean> containsUUIDAsync(UUID uuid);
    public abstract CompletableFuture<Void> syncOneEntryAsync(Entry entry);
    public abstract void syncEntries();

}
