package net.il0c4l.seasons.storage;


import net.il0c4l.seasons.Tier;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class Entry implements ConfigurationSerializable {

    private UUID uuid;
    private double points;
    private boolean completed;
    private List<SubEntry> subEntries;

    public Entry(UUID uuid){
        this.uuid = uuid;
        subEntries = new ArrayList<>();
        this.completed = false;
    }

    public Entry(UUID uuid, List<SubEntry> subEntries, double points){
        this.uuid = uuid;
        this.subEntries = subEntries;
        this.points = points;
        this.completed = false;
    }

    public Entry(UUID uuid, boolean completed){
        this.uuid = uuid;
        this.completed = completed;
    }

    public Entry(Map<String, Object> serialized){
        this.points = (double) serialized.get("points");
        this.completed = (boolean) serialized.get("completed");
        this.uuid = UUID.fromString((String) serialized.get("uuid"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mappedSubEntries = (List<Map<String, Object>>) serialized.get("subentries");

        this.subEntries = mappedSubEntries.stream().map(it -> new SubEntry(it, uuid)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> serialize(){
        HashMap<String, Object> serializer = new HashMap<>();

        serializer.put("uuid", uuid.toString());
        serializer.put("points", points);
        serializer.put("completed", completed);


        List<Map<String, Object>> serSubEntries = subEntries.stream().map(SubEntry::serialize).collect(Collectors.toList());
        serializer.put("subentries", serSubEntries);
        return serializer;
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

    public List<SubEntry> getActiveChallenges(){
        return subEntries;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean complete){
        this.completed = complete;
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

    public SubEntry getSubEntry(String command) {
        return subEntries.stream().filter(match -> match.getCommand().equals(command)).findAny().orElse(null);
    }

}
