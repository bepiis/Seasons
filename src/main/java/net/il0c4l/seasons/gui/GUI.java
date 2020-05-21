package net.il0c4l.seasons.gui;

import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.StaticGuiElement;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import de.themoep.inventorygui.InventoryGui;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class GUI{

    private final Main plugin;
    private InventoryGui gui;

    public GUI(final Main plugin){
        this.plugin = plugin;
        initialize();
    }

    public void initialize(){
        String[] guiSetup = {
          "qqqqqqqqq",
          "ggkggglgg",
          "fffffffff",
          "asggpggdr"
        };

        gui = new InventoryGui(plugin, null, "Tiers", guiSetup);

        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("gui");

        gui.addElement(new GuiPageElement('a', new ItemStack(Material.valueOf(sec.getString("previous"))), GuiPageElement.PageAction.PREVIOUS, "Previous page (%prevpage%)"));
        gui.addElement(new GuiPageElement('s', new ItemStack(Material.valueOf(sec.getString("first"))), GuiPageElement.PageAction.FIRST, "First page (current: %page%"));
        gui.addElement(new GuiPageElement('d', new ItemStack(Material.valueOf(sec.getString("last"))), GuiPageElement.PageAction.LAST, "Last page (%pages%)"));
        gui.addElement(new GuiPageElement('r', new ItemStack(Material.valueOf(sec.getString("next"))), GuiPageElement.PageAction.NEXT, "Next page (%nextpage%)"));

        List<Tier> tierList = plugin.getConfigHandler().getTierList();
        int cnt = 0;
        final char[] slots = {'k', 'p', 'l'};
        for(int idx=3; idx<tierList.size(); idx+=3){
            for(int i=idx-3; i<idx; i--){
                gui.addElement(new StaticGuiElement(slots[i], tierList.get(i).getGuiItem()));
                cnt++;
            }
        }
        if(tierList.size() % 3 != 0){
            if(tierList.size()-cnt == 2){
                gui.addElement(new StaticGuiElement(slots[0], tierList.get(cnt+2).getGuiItem()));
            }
            gui.addElement(new StaticGuiElement(slots[1], tierList.get(cnt+1).getGuiItem()));
        }
        gui.build();
    }

    public InventoryGui getGui(){
        return gui;
    }







}
