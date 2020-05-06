package net.il0c4l.seasons.storage;

import net.il0c4l.seasons.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FlatDataHandler extends DataHandler {

    private final transient Main plugin;
    private final transient Logger logger;
    private File file;
    private FileConfiguration data;
    private transient Executor executor;

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

    @Override
    protected CompletableFuture<List<Entry>> getEntriesFromStorageAsync(){
        return CompletableFuture.supplyAsync(() ->
                data.getConfigurationSection("UUID").getKeys(false).stream().map(it ->
                        (Entry) data.get("UUID." + it)).collect(Collectors.toList()), executor);
    }

    @Override
    public void syncEntries(){
        entries.forEach(it -> {
            it.getActiveChallenges().removeIf(SubEntry::isDone);
            data.set("UUID." + it.getUUID().toString(), it);
        });
        saveEntries();
    }

    @Override
    public CompletableFuture<Void> syncOneEntryAsync(Entry entry){
        return CompletableFuture.runAsync(() -> {
            data.set("UUID." + entry.getUUID().toString(), entry);
            saveEntries();
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> containsUUIDAsync(UUID uuid){
        return CompletableFuture.supplyAsync(() -> data.contains("UUID." + uuid.toString()), executor);
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

    public void saveEntries() {
        try{
            data.save(file);
        } catch(IOException e){
            logger.log(Level.SEVERE, IO_ERROR);
        }
    }

    private static final String NEW_FILE = "No flat storage file found! I'm going to try to create one for you...";
    private static final String IO_ERROR = "Something has gone wrong while handling a storage file! Check write permissions maybe?";
}
