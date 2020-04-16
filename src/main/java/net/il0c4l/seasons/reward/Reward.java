package net.il0c4l.seasons.reward;

import org.bukkit.entity.Player;

public interface Reward {

    String getType();

    void giveReward(Player player);
}
