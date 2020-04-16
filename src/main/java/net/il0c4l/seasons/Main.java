package net.il0c4l.seasons;

import net.il0c4l.seasons.commands.SeasonsCommand;
import net.il0c4l.seasons.listener.EntityDeathListener;
import net.il0c4l.seasons.listener.PlayerLoginListener;
import net.il0c4l.seasons.storage.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static int T_NUM;
    private Executor executor;
    private ConfigHandler configHandler;
    private DataHandler storage;

    @Override
    public void onEnable(){
        saveDefaultConfig();
        executor = startExecutorService();
        configHandler = new ConfigHandler(this);
        setStorageType();
        new EntityDeathListener(this);
        new PlayerLoginListener(this, storage);
        new SeasonsCommand(this,"seadmin");
        aEntrySync();
    }

    @Override
    public void onDisable(){

    }

    public Executor startExecutorService(){
        return Executors.newFixedThreadPool(T_NUM=5, runnable -> {
            int count = 0;
            return new Thread(runnable, "seasons-executor-" + ++count);
        });
    }

    public void setStorageType(){
        String type = DataHandler.getStorageType(getConfig()).toLowerCase();
        switch(type){
            case "mysql":
                Map<String, Object> creds = getConfig().getConfigurationSection("storage.mysql").getValues(false);
                storage = new MySQLDataHandler(this, (String) creds.get("username"), (String) creds.get("password"), (String) creds.get("host"), (String) creds.get("port"), (String) creds.get("dbname"), (String) creds.get("useSSL"));
                getLogger().log(Level.INFO, "CONNECTED");
                break;
            case "sqlite":
                storage = new SQLiteDataHandler(this);
                break;
            case "yaml":
                storage = new FlatDataHandler(this, "data.yml");
                break;
            case "default":
                getLogger().log(Level.WARNING, "No storage type chosen. Defaulting to flat file storage!");
                getConfig().set("storage.yaml.type", true);
                storage = new FlatDataHandler(this, "data.yml");
                type = "yaml";
                break;
        }
        getLogger().log(Level.INFO, "Set storage type to " + type);
    }

    public Executor getThreadPool(){ return executor; }

    public ConfigHandler getConfigHandler(){
        return configHandler;
    }

    public DataHandler getDataHandler(){
        return storage;
    }

    public int getNumThreads(){ return T_NUM; }

    private void aEntrySync(){
        Runnable run = () -> {
            try{
                Thread.sleep(15000L);
            } catch(InterruptedException e){
                e.printStackTrace();
            } finally{
                storage.syncEntries();
                getLogger().log(Level.INFO, "SYNCED");
            }
        };
        executor.execute(() -> {
            while(isEnabled()) { run.run(); }
        });

    }

    public void sendMessage(Player player, String message){
        String prefix = getConfigHandler().getPrefix() + " ";
        player.sendMessage(Utils.chat(prefix + message.replace("{PLAYER}", player.getName())));
    }
}
