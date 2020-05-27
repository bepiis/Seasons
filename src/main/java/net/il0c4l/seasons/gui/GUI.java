package net.il0c4l.seasons.gui;

import de.themoep.inventorygui.*;
import net.il0c4l.seasons.ConfigHandler;
import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Tier;
import net.il0c4l.seasons.Utils;
import net.il0c4l.seasons.storage.Entry;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GUI{

    private final Main plugin;
    private final List<StaticGuiElement> states;
    private InventoryGui gui;

    public GUI(final Main plugin){
        this.plugin = plugin;
        this.states = initialize();
    }

    private List<StaticGuiElement> initialize(){
        String[] guiSetup = {
          "aaaaaaaaa",
          "gkgggggkg",
          "ooooooooo",
          "ggggkgggg",
          "aaaaaaaaa",
          "fpgggggnl"
        };

        gui = new InventoryGui(plugin, null, "Tiers", guiSetup);

        List<Tier> tierList = plugin.getConfigHandler().getTierList();

        GuiElementGroup group = new GuiElementGroup('k');
        tierList.forEach(it -> group.addElement(new StaticGuiElement('e', new ItemStack(it.getGuiItem()))));
        gui.addElement(group);


        List<StaticGuiElement> states = new ArrayList<>();
        GuiElementGroup outline = new GuiElementGroup('a');
        for(int i=0; i<tierList.size()*6; i++){
            outline.addElement(new StaticGuiElement('z', ConfigHandler.progBarMats.get("outline")));
        }
        gui.addElement(outline);

        GuiElementGroup grp = new GuiElementGroup('o');
        for(int i=0; i<tierList.size()*3; i++){
            StaticGuiElement stack = new StaticGuiElement('z', ConfigHandler.progBarMats.get("notfinished"));
            grp.addElement(stack);
            states.add(stack);
        }
        gui.addElement(grp);

        gui.addElement(new GuiPageElement('p', new ItemStack(ConfigHandler.progBarMats.get("previous")), GuiPageElement.PageAction.PREVIOUS, "Previous page (%prevpage%)"));
        gui.addElement(new GuiPageElement('f', new ItemStack(ConfigHandler.progBarMats.get("first")), GuiPageElement.PageAction.FIRST, "First page (current: %page%"));
        gui.addElement(new GuiPageElement('l', new ItemStack(ConfigHandler.progBarMats.get("last")), GuiPageElement.PageAction.LAST, "Last page (%pages%)"));
        gui.addElement(new GuiPageElement('n', new ItemStack(ConfigHandler.progBarMats.get("next")), GuiPageElement.PageAction.NEXT, "Next page (%nextpage%)"));

        gui.build();
        return states;
    }

    public InventoryGui getGui() {
        return gui;
    }

    public void updateProgBar(Entry entry){
        double percent = entry.getPoints()/ConfigHandler.totalPoints;
        int progBars = (int) (3*ConfigHandler.tierList.size()*(percent));
        int idx = 0;

        GuiElementGroup grp = new GuiElementGroup('o');
        while(idx < ConfigHandler.tierList.size()*3){
            if(idx < progBars){
                states.get(idx).setItem(ConfigHandler.progBarMats.get("finished"));
                states.get(idx).setText(" ");
            }
            grp.addElement(states.get(idx));
            idx++;
        }
        states.get(progBars).setItem(ConfigHandler.progBarMats.get("current"));
        states.get(progBars).setText(Utils.chat("&7" + percent + "% complete"));
        grp.addElement(states.get(progBars));

        gui.addElement(grp);
        gui.draw(Bukkit.getPlayer(entry.getUUID()));
    }












}
