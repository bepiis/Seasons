package net.il0c4l.seasons.event;

import net.il0c4l.seasons.storage.Entry;

public class PointChangeEvent extends ChallengeEvent {

    private double points;
    private Entry entry;

    public PointChangeEvent(double points, Entry entry){
        super(entry);
        this.points = points;
        this.entry = entry;
    }

    public double getPoints(){
        return points;
    }

    public double getPastPoints(){
        return entry.getPoints();
    }

}
