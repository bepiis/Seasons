package net.il0c4l.seasons.commands;

import net.il0c4l.seasons.Main;
import net.il0c4l.seasons.Utils;
import net.il0c4l.seasons.event.PointChangeEvent;
import net.il0c4l.seasons.listener.AbstractListener;
import net.il0c4l.seasons.storage.Entry;
import net.il0c4l.seasons.storage.SubEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.UUID;

public class SeasonsCommand extends Command {

    private Main plugin;
    private String[] args;
    private CommandSender sender;

    public SeasonsCommand(Main plugin, String cmd){
        super(cmd);
        Utils.registerCommand(this);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args){
        this.args = args;
        this.sender = sender;
        switch(args[0].toLowerCase()){
            case "setchallenge":
                if (!setChallenge())
                    return false;
                break;
            case "setpoints":
                if(!setPoints())
                    return false;
                break;
            default:
                plugin.sendMessage(sender, "I do not recognize this command.");
                return false;
        }
        return true;
    }

    public boolean setChallenge(){
        if(args.length < 4){
            plugin.sendMessage(sender, INCORRECT_NUM_ARGUMENTS);
            return false;
        }
        String command = "";

        if(args[3].equalsIgnoreCase("EntityDeath")){
            StringBuilder builder = new StringBuilder("EntityDeath.EntityType:");
            if(args.length - 4 >= 1 && args.length - 4 <= 2){
                builder.append(args[4]);
                if(args.length - 4 == 2){
                    builder.append(".Material:").append(args[5]);
                }
            } else {
                plugin.sendMessage(sender, INCORRECT_NUM_ARGUMENTS);
                return false;
            }
            command = builder.toString();
        }

        Entry entry;
        UUID uuid = Bukkit.getServer().getPlayer(args[1]).getUniqueId();
        int progress = Integer.parseInt(args[2]);

        if(!plugin.getDataHandler().entryExists(uuid)){
            entry = new Entry(uuid);
        } else {
            entry = plugin.getDataHandler().getEntry(uuid);
        }

        entry.addSubEntry(new SubEntry(uuid, command, progress, false));
        plugin.sendMessage(sender, plugin.getServer().getPlayer(uuid), "Challenge set for {PLAYER}.");
        return true;
    }

    public boolean setPoints(){
        if(args.length != 3){
            plugin.sendMessage(sender, INCORRECT_NUM_ARGUMENTS);
            return false;
        }

        double points = Double.parseDouble(args[2]);
        if(points > plugin.getConfigHandler().getTotalPoints()){
            plugin.sendMessage(sender, "Points set must be less than total points!");
        }

        UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();

        Entry entry;
        if(!plugin.getDataHandler().entryExists(uuid)){
            entry = new Entry(uuid);
        } else {
            entry = plugin.getDataHandler().getEntry(uuid);
        }

        AbstractListener.call(plugin, new PointChangeEvent(uuid, points, entry));
        plugin.sendMessage(sender, Bukkit.getPlayer(uuid), "Set {PLAYER}'s points to: " + points);
        return true;
    }

    public static final String INCORRECT_NUM_ARGUMENTS = "Incorrect number of arguments.";

}


