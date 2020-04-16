package net.il0c4l.seasons.storage;

import net.il0c4l.seasons.Main;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLiteDataHandler extends DataHandler {

    private Main plugin;

    public SQLiteDataHandler(final Main plugin){
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    protected ArrayList<Entry> getEntriesFromStorage() {
        return null;
    }

    @Override
    public void syncEntries() {

    }

    @Override
    public CompletableFuture<ArrayList<Entry>> getEntriesFromStorageAsync(){
        return null;
    }

    @Override
    public CompletableFuture<Boolean> containsUUIDAsync(UUID uuid){
        return null;
    }

    @Override
    public CompletableFuture<Void> syncOneEntryAsync(Entry entry){ return null; }
}
