package net.il0c4l.seasons.event;

import net.il0c4l.seasons.storage.Entry;
import org.bukkit.entity.Player;

public class PointChangeEvent extends ChallengeEvent {

    private double points;
    private Entry entry;

    public PointChangeEvent(Player player, double points, Entry entry){
        super(player);
        this.points = points;
        this.entry = entry;
    }

    public double getPoints(){
        return points;
    }

    public double getPastPoints(){
        return entry.getPoints();
    }

    public Entry getEntry(){
        return entry;
    }
}
