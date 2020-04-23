package net.il0c4l.seasons.storage;


import java.util.ArrayList;
import java.util.UUID;

public class Entry {

    private UUID uuid;
    private double points;
    private boolean completed;
    private ArrayList<SubEntry> subEntries;

    public Entry(UUID uuid){
        this.uuid = uuid;
        subEntries = new ArrayList<>();
        this.completed = false;
    }

    public Entry(UUID uuid, ArrayList<SubEntry> subEntries, double points){
        this.uuid = uuid;
        this.subEntries = subEntries;
        this.points = points;
        this.completed = false;
    }

    public Entry(UUID uuid, boolean completed){
        this.uuid = uuid;
        this.completed = completed;
    }

    public UUID getUUID(){
        return uuid;
    }

    public double getPoints(){
        return points;
    }

    public void setPoints(double points){
        this.points = points;
    }

    public ArrayList<SubEntry> getActiveChallenges(){
        return subEntries;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean complete){
        this.completed = complete;
    }

    public boolean equals(Entry entry){
        return entry.getUUID().toString().equals(uuid.toString());
    }

    public void addSubEntry(SubEntry subEntry){
        subEntries.add(subEntry);
    }

    public void updateSubEntry(SubEntry subEntry){
        if(!subEntries.contains(subEntry)){
            return;
        }
        subEntries.set(subEntries.indexOf(subEntry), subEntry);
    }

    public SubEntry getSubEntry(String command){
        return subEntries.stream().filter(match -> match.getCommand().equals(command)).findAny().orElse(null);
    }

}
