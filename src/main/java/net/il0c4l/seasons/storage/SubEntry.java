package net.il0c4l.seasons.storage;

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

    public boolean equals(SubEntry subEntry){
        if(getUUID().toString().equals(subEntry.getUUID().toString()) && command.equals(subEntry.getCommand())){
            return true;
        }
        return false;
    }
}
