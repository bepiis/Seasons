package net.il0c4l.seasons.commands;

import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Utils;
import net.il0c4l.seasons.storage.Entry;
import net.il0c4l.seasons.storage.SubEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SeasonsCommand extends Command {

    private Main plugin;

    public SeasonsCommand(Main plugin, String cmd){
        super(cmd);
        Utils.registerCommand(this);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args){
        if(args[0].equalsIgnoreCase("setChallenge")){
            if(args.length != 4){
                commandSender.sendMessage("Incorrect number of arguments. Expected 3, got " + args.length);
                return false;
            }
            UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
            String command = args[2];
            int progress = Integer.parseInt(args[3]);
            Entry entry;
            if(!plugin.getDataHandler().entryExists(uuid)){
                entry = new Entry(uuid);
            } else{
                entry = plugin.getDataHandler().getEntry(uuid);
            }
            entry.addSubEntry(new SubEntry(uuid, command, progress, false));
            commandSender.sendMessage("Challenge set for " + Bukkit.getPlayer(uuid).toString());
            return true;
        }
        return false;
    }
}
