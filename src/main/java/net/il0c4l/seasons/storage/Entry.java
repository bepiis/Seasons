package net.il0c4l.seasons.storage;

import java.util.ArrayList;
import java.util.UUID;

public class Entry {

    private UUID uuid;
    private int points;
    private boolean completed;
    private ArrayList<SubEntry> subEntries;

    public Entry(UUID uuid){
        this.uuid = uuid;
        subEntries = new ArrayList<>();
        this.completed = false;
    }

    public Entry(UUID uuid, ArrayList<SubEntry> subEntries, int points){
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

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
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
        if(entry.getUUID().toString().equals(uuid.toString())){
            return true;
        }
        return false;
    }

    public void addSubEntry(SubEntry subEntry){
        subEntries.add(subEntry);
    }

    public void removeSubEntry(SubEntry subEntry){
        subEntries.remove(subEntry);
    }

    public int indexOfSubEntry(SubEntry subEntry){
        int found = -1;
        for(int i=0; i<subEntries.size(); i++){
            if(subEntry.equals(subEntries.get(i))){
                found = i;
            }
        }
        return found;
    }

    public void updateSubEntry(SubEntry subEntry){
        subEntries.forEach(iter -> {
            if(iter.equals(subEntry)){
                iter = subEntry;
            }
        });
    }

    public SubEntry getSubEntry(String command){
        SubEntry subEntry = new SubEntry(uuid, "None", 0, false);
        for(SubEntry subEntryIter : subEntries){
            if(subEntryIter.getCommand().equals(command)){
                subEntry = subEntryIter;
            }
        }
        return subEntry;
    }

}
