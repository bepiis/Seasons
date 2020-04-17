package net.il0c4l.seasons;

import net.il0c4l.seasons.reward.Reward;
import java.util.List;

public class Tier {

    private List<Reward> rewards;
    private List<String> lore, messages;
    private String title, guiItem;

    public Tier(String title, List<String> lore, String guiItem, List<Reward> rewards, List<String> messages) {
        lore.forEach(Utils::chat);

        this.rewards = rewards;
        this.title = Utils.chat(title);
        this.guiItem = guiItem;
        this.lore = lore;
        this.messages = messages;
    }

    public List<Reward> getRewards(){
        return rewards;
    }

    public List<String> getMessages(){
        return messages;
    }
}
