package net.il0c4l.seasons.storage;

import net.il0c4l.seasons.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDataHandler extends DataHandler {

    private final String URL, USERNAME, PASSWORD;
    private Main plugin;
    private static Connection connection;
    private final Logger logger;
    private final String tableName;

    public MySQLDataHandler(final Main plugin, String username, String password, String host, String port, String dbName, String useSSL){
        super(plugin);
        this.plugin = plugin;
        this.PASSWORD = password;
        this.USERNAME = username;
        URL = String.format("jdbc:mysql://%1$s:%2$s/%3$s?allowPublicKeyRetrieval=true&useSSL=%4$s", host, port, dbName, useSSL);
        logger = plugin.getLogger();
        openConnection();
        tableName = "seasons";
        createTable();
    }

    public void openConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, DRIVER_NOT_FOUND);
            return;
        }
        try{
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch(SQLException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, SQL_EXCEPTION);
        }
    }
    
    public void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            } 
        }catch(SQLException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, SQL_EXCEPTION);
        }
    }
    
    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + 
                "uuid VARCHAR(64) NOT NULL, " +
                "command VARCHAR(64) NOT NULL, " + 
                "progress INT NOT NULL, " + 
                "PRIMARY KEY ( uuid ) " +
        ");";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, SQL_EXCEPTION);
        }
    }
    
    @Override
    public void syncEntries(){
        CompletableFuture.runAsync(() -> {
            String sql = "INSERT INTO " + tableName + " (uuid, command, progress) VALUES (? ,?, ?)";
            try{
                PreparedStatement ps = connection.prepareStatement(sql);
                
                final int batchSize = 1000;
                int count = 0;
                
                for(Entry entry : super.entries){
                    ps.setString(1, entry.getUUID().toString());
                   // ps.setString(2, entry.getCommand());
                  //  ps.setInt(3, entry.getProgress());
                    ps.addBatch();
                    
                    if(++count % batchSize == 0){
                        ps.executeBatch();
                    }
                }
                ps.executeBatch();
                ps.close();
            } catch(SQLException e){
                e.printStackTrace();
                logger.log(Level.SEVERE, SQL_EXCEPTION);
            }
        }, plugin.getThreadPool());
    }

    @Override
    public ArrayList<Entry> getEntriesFromStorage() {
        ArrayList<Entry> entries = new ArrayList<>();
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String command = result.getString("command");
                int progress = result.getInt("progress");

                //entries.add(new Entry(uuid, command, progress));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, SQL_EXCEPTION);
        }
        return entries;
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
    
    public static String DRIVER_NOT_FOUND = "jbdc driver not loaded!";
    public static String SQL_EXCEPTION = "An error occurred while communicating with your sql server!";

}
