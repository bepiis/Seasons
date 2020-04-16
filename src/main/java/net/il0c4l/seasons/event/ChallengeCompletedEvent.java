package net.il0c4l.seasons.event;

import org.bukkit.entity.Player;

public final class ChallengeCompletedEvent extends ChallengeEvent {

    private int earned, current;

    public ChallengeCompletedEvent(Player player, int earned, int current){
        super(player);
        this.earned = earned;
        this.current = current;
    }

    public int getEarned(){
        return earned;
    }

    public int getCurrent(){
        return current;
    }
}
