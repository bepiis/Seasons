package net.il0c4l.seasons;

import net.il0c4l.seasons.reward.Reward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Tier {

    private List<Reward> rewards;
    private List<String> lore, messages;
    private String title, guiItem;

    public Tier(String title, List<String> lore, String guiItem, List<Reward> rewards, List<String> messages) {
        this.rewards = rewards;
        this.title = Utils.chat(title);
        this.guiItem = guiItem;
        this.lore = Utils.chat(lore);
        this.messages = messages;
    }

    public ItemStack getGuiItem(){
        ItemStack stack = new ItemStack(Material.valueOf(guiItem), 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(Utils.chat(lore));
        meta.setDisplayName(Utils.chat(title));
        stack.setItemMeta(meta);
        return stack;
    }

    public List<Reward> getRewards(){
        return rewards;
    }

    public List<String> getMessages(){
        return messages;
    }
}
