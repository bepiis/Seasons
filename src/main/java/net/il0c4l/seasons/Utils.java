package net.il0c4l.seasons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static String chat(String s) { return ChatColor.translateAlternateColorCodes('&', s); }

    public static List<String> chat(List<String> s){
        return s.stream().map(Utils::chat).collect(Collectors.toList());
    }

    public static void registerCommand(Command command){
        try{
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);
        } catch(final Exception e){
            e.printStackTrace();
        }
    }

    public static String missingProperty(String prop){
        return "Your configuration is missing a key property: " + prop;
    }
}
