package net.il0c4l.seasons.reward;

import net.il0c4l.seasons.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public final class CommandReward implements Reward {

    private List<String> commands;

    public CommandReward(List<String> commands){
        this.commands = commands;
    }

    @Override
    public String getType() {
        return "command";
    }

    @Override
    public void giveReward(Player player) {
        Server server = Bukkit.getServer();
        CommandSender sender = Bukkit.getConsoleSender();

        commands.forEach(it -> {
            String updated = it.replace("{PLAYER}", player.getName());
            server.dispatchCommand(sender, Utils.chat(updated));
        });
    }
}
