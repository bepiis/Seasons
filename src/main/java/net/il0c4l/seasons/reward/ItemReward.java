package net.il0c4l.seasons.reward;

import net.il0c4l.seasons.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemReward implements Reward {

    private ItemStack item;

    public ItemReward(Material material, int amount, String displayName, List<String> lore, List<String> enchants){
        item = new ItemStack(material);
        item.setAmount(amount);
        ItemMeta itemMeta = item.getItemMeta();
        if(displayName.equals("")){
            itemMeta.setDisplayName(Utils.chat(displayName));
        }
        if(!lore.isEmpty()){
            lore.forEach(Utils::chat);
            itemMeta.setLore(lore);
        }
        if(!enchants.isEmpty()){
            Map<Enchantment, Integer> enchantList = new HashMap<>();
            enchants.forEach(it -> {
                String[] split = it.split(":");
                NamespacedKey key = NamespacedKey.minecraft(split[0]);
                enchantList.put(Enchantment.getByKey(key), Integer.parseInt(split[1]));
            });
            item.addEnchantments(enchantList);
        }
        item.setItemMeta(itemMeta);
    }

    public ItemStack getItem(){
        return item;
    }

    @Override
    public void giveReward(Player player){
        //todo check if inventory full
        //if(player.getInventory().firstEmpty() == -1)
        player.getInventory().addItem(item);
    }

    @Override
    public String getType() {
        return "item";
    }
}
