package net.il0c4l.seasons.storage;

import net.il0c4l.seasons.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlatDataHandler extends DataHandler {

    private final Main plugin;
    private final Logger logger;
    private File file;
    private FileConfiguration data;
    private Executor executor;


    public FlatDataHandler(final Main plugin, String fileName){
        super(plugin);
        this.plugin = plugin;
        this.executor = plugin.getThreadPool();
        logger = plugin.getLogger();
        try{
            data = returnFlatFileAsync(fileName).get();
            if(!data.contains("UUID")){
                data.createSection("UUID");
            }
            saveEntries();
            super.entries = getEntriesFromStorageAsync().get();
        } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

    }

    protected FileConfiguration returnFlatFile(String fileName){
        file = new File(plugin.getDataFolder().getPath(), fileName);
        if(!file.exists()){
            logger.log(Level.INFO, NEW_FILE);
            try{
                file.createNewFile();
                logger.log(Level.INFO, "Done.");
            } catch(IOException e){
                e.printStackTrace();
                logger.log(Level.SEVERE, IO_ERROR);
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    protected CompletableFuture<FileConfiguration> returnFlatFileAsync(String fileName){
        return CompletableFuture.supplyAsync(() -> {
            file = new File(plugin.getDataFolder().getPath(), fileName);
            if(!file.exists()){
                logger.log(Level.INFO, NEW_FILE);
                try{
                    file.createNewFile();
                    logger.log(Level.INFO, "Done.");
                } catch(IOException e){
                    e.printStackTrace();
                    logger.log(Level.SEVERE, IO_ERROR);
                }
            }
            return YamlConfiguration.loadConfiguration(file);
        }, executor);
    }

    @Override
    protected ArrayList<Entry> getEntriesFromStorage() {
        ArrayList<Entry> entries = new ArrayList<>();

        for(String sec : data.getConfigurationSection("UUID").getKeys(false)){
            ArrayList<SubEntry> subEntries = new ArrayList<>();
            String activePath = "UUID." + sec;

            if(data.getBoolean(activePath + ".completed")){
                entries.add(new Entry(UUID.fromString(sec), true));
                continue;
            }

            double points = data.getDouble(activePath + ".points");

            for(String subSec : data.getConfigurationSection(activePath).getKeys(false)){
                int progress = data.getInt(activePath + ".Active." + subSec);
                subEntries.add(new SubEntry(UUID.fromString(sec), subSec, progress, false));
            }
            entries.add(new Entry(UUID.fromString(sec), subEntries, points));

        }
        return entries;
    }

    @Override
    protected CompletableFuture<ArrayList<Entry>> getEntriesFromStorageAsync(){
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<Entry> entries = new ArrayList<>();

            for(String sec : data.getConfigurationSection("UUID").getKeys(false)){
                ArrayList<SubEntry> subEntries = new ArrayList<>();
                String activePath = "UUID." + sec;

                if(data.getBoolean(activePath + ".completed")){
                    entries.add(new Entry(UUID.fromString(sec), true));
                    continue;
                }

                double points = data.getDouble(activePath + ".points");

                for(String subSec : data.getConfigurationSection(activePath + ".Active").getKeys(false)){
                    int progress = data.getInt(activePath + ".Active." + subSec);
                    subEntries.add(new SubEntry(UUID.fromString(sec), subSec.replace('-', '.'), progress, false));
                }
                entries.add(new Entry(UUID.fromString(sec), subEntries, points));
            }
            return entries;
        }, executor);
    }

    public void saveEntries() {
        try{
            data.save(file);
        } catch(IOException e){
            logger.log(Level.SEVERE, IO_ERROR);
        }
    }

    public CompletableFuture<Void> saveEntriesAsync(){
        return CompletableFuture.runAsync(() -> {
            try{
                data.save(file);
            } catch(IOException e){
                e.printStackTrace();
            }
        }, executor);
    }

    @Override
    public void syncEntries(){
        for(Entry entry : super.entries){
            if(!data.contains("UUID." + entry.getUUID().toString())){
                data.createSection("UUID." + entry.getUUID().toString() + ".Active");
            }

            ConfigurationSection sec = data.getConfigurationSection("UUID." + entry.getUUID().toString());

            if(entry.isCompleted()){
                sec.set("complete", true);
                sec.set("Active", null);
                continue;
            }

            sec.set("points", entry.getPoints());

            Iterator<SubEntry> it = entry.getActiveChallenges().iterator();
            while(it.hasNext()){
                SubEntry subEntry = it.next();
                String command = subEntry.getCommand().replace('.', '-');
                if(subEntry.isDone()){
                    sec.set("Active." + command, null);
                    it.remove();
                    continue;
                }

                sec.set("Active." + command, subEntry.getProgress());
            }
        }
        saveEntries();
    }

    @Override
    public CompletableFuture<Void> syncOneEntryAsync(Entry entry){
        return CompletableFuture.runAsync(() -> {
            String path = "UUID." + entry.getUUID().toString();

            if(entry.isCompleted()){
                data.set(path + ".complete", true);
                data.set(path + ".Active", null);
            } else {
                entry.getActiveChallenges().forEach(iter -> {
                    String command = iter.getCommand().replace('.', '-');
                    data.set(path + ".Active." + command, iter.getProgress());
                });
                data.set(path + ".points", entry.getPoints());
            }
            saveEntries();
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> containsUUIDAsync(UUID uuid){
        return CompletableFuture.supplyAsync(() -> data.contains("UUID." + uuid.toString()), executor);
    }

    private static final String NEW_FILE = "No flat storage file found! I'm going to try to create one for you...";
    private static final String IO_ERROR = "Something has gone wrong while handling a storage file! Check write permissions maybe?";
}
