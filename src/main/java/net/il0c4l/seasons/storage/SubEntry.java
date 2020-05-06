package net.il0c4l.seasons.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubEntry extends Entry{

    private String command;
    private int progress;
    private boolean done;

    public SubEntry(UUID uuid, String command, int progress, boolean done){
        super(uuid);
        this.command = command;
        this.progress = progress;
        this.done = done;
    }

    public SubEntry(Map<String, Object> serialized, UUID uuid){
        super(uuid);
        this.command = (String) serialized.get("command");
        this.progress = (int) serialized.get("progress");
        this.done = false;
    }

    @Override
    public Map<String, Object> serialize(){
        HashMap<String, Object> serializer = new HashMap<>();

        serializer.put("command", command);
        serializer.put("progress", progress);

        return serializer;
    }

    public String getCommand(){
        return command;
    }

    public int getProgress(){
        return progress;
    }

    public boolean isDone(){
        return done;
    }

    public void setDone(boolean done){
        this.done = done;
    }

    public void setProgress(int progress){
        this.progress = progress;
    }

    public void setCommand(String command){
        this.command = command;
    }

}
