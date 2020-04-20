package net.il0c4l.seasons.reward;

import net.il0c4l.seasons.listener.AbstractListener;
import org.bukkit.entity.Player;

public interface Reward {

    String getType();

    void giveReward(Player player);
    
}
