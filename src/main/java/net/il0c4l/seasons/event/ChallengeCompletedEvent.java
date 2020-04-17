package net.il0c4l.seasons.event;

import net.il0c4l.seasons.Challenge;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.entity.Player;

public final class ChallengeCompletedEvent extends ChallengeEvent {

    private Entry entry;
    private final Challenge completed;

    public ChallengeCompletedEvent(Player player, Entry entry, Challenge completed){
        super(player);
        this.entry = entry;
        this.completed = completed;
    }

    public Entry getEntry(){
        return entry;
    }

    public Challenge getChallenge(){
        return completed;
    }


}
