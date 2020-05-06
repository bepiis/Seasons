package net.il0c4l.seasons.event;

import net.il0c4l.seasons.storage.Entry;

public class ChallengeEvent extends SeasonsEvent {

    private Entry entry;

    public ChallengeEvent(Entry entry){
        super(entry.getUUID());
        this.entry = entry;
    }

    public Entry getEntry(){
        return entry;
    }
}
