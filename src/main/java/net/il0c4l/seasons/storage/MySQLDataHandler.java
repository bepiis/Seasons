package net.il0c4l.seasons.storage;

import jdk.internal.loader.AbstractClassLoaderValue;
import net.il0c4l.seasons.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDataHandler extends DataHandler {

    private final String URL, USERNAME, PASSWORD;
    private Main plugin;
    private static Connection connection;
    private final Logger logger;
    private final String entryTableName, subEntryTableName;

    public MySQLDataHandler(final Main plugin, String username, String password, String host, String port, String dbName, String useSSL){
        super(plugin);
        this.plugin = plugin;
        this.PASSWORD = password;
        this.USERNAME = username;
        URL = String.format("jdbc:mysql://%1$s:%2$s/%3$s?allowPublicKeyRetrieval=true&useSSL=%4$s", host, port, dbName, useSSL);
        logger = plugin.getLogger();
        openConnection();
        entryTableName = "entries";
        subEntryTableName = "subentries";
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
        String sql = "CREATE TABLE IF NOT EXISTS " + entryTableName + "(" +
                "uuid VARCHAR(64) NOT NULL, " +
                "points DOUBLE NOT NULL, " +
                "PRIMARY KEY ( uuid ) " +
        ");" +
                "CREATE TABLE IF NOT EXISTS " + subEntryTableName + "(" +
                "uuid VARCHAR(64) NOT NULL, " +
                "command VARCHAR(64) NOT NULL," +
                "progress INT NOT NULL," +
                "PRIMARY KEY ( uuid ), " +
                "FOREIGN KEY ( uuid ) REFERENCES " + entryTableName + "( uuid )" +
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
    public CompletableFuture<Void> syncOneEntryAsync(Entry entry){
        return CompletableFuture.runAsync(() -> {
            String stmt = "INSERT INTO " + entryTableName + " (uuid, points) VALUES (?, ?)";
            String subStmt = "INSERT INTO " + subEntryTableName + " (uuid, command, progress) VALUES (?, ?, ?)";

            try{
                PreparedStatement ps = connection.prepareStatement(stmt);
                ps.setString(1, entry.getUUID().toString());
                ps.setDouble(2, entry.getPoints());

                ps.executeUpdate();
                ps.close();

                PreparedStatement pss = connection.prepareStatement(subStmt);
                for(SubEntry subEntry : entry.getActiveChallenges()){
                    pss.setString(1, subEntry.getUUID().toString());
                    pss.setString(2, subEntry.getCommand());
                    pss.setInt(3, subEntry.getProgress());
                }

                pss.executeUpdate();
                pss.close();

        } catch(SQLException e){
                e.printStackTrace();
                logger.log(Level.SEVERE, SQL_EXCEPTION);
            }
        }, plugin.getThreadPool());
    }
    
    @Override
    public void syncEntries(){
        CompletableFuture.runAsync(() -> {
            String sql = "INSERT INTO " + entryTableName + " (uuid, command, progress) VALUES (? ,?, ?)";
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
    public CompletableFuture<List<Entry>> getEntriesFromStorageAsync(){
        return CompletableFuture.supplyAsync(() -> {
            List<Entry> entries = new ArrayList<>();
            try{
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM " +  entryTableName);
                ResultSet result = ps.executeQuery();

                while(result.next()){
                    String uuid = result.getString("uuid");
                    double points = result.getDouble("points");

                    PreparedStatement pss = connection.prepareStatement("SELECT * FROM " + subEntryTableName + " WHERE uuid = " + uuid);
                    ResultSet subResult = pss.executeQuery();
                    List<SubEntry> subEntries = new ArrayList<>();
                    while(subResult.next()){
                        String command = subResult.getString("command");
                        int progress = subResult.getInt("progress");
                        subEntries.add(new SubEntry(UUID.fromString(uuid), command, progress, false));
                    }
                    entries.add(new Entry(UUID.fromString(uuid), subEntries, points));
                }
            } catch(SQLException e){
                e.getErrorCode();
                logger.log(Level.SEVERE, SQL_EXCEPTION);
            }
            return entries;
        }, plugin.getThreadPool());
    }

    @Override
    public CompletableFuture<Boolean> containsUUIDAsync(UUID uuid){
        return null;
    }


    
    public static String DRIVER_NOT_FOUND = "jbdc driver not loaded!";
    public static String SQL_EXCEPTION = "An error occurred while communicating with your sql server!";

}
