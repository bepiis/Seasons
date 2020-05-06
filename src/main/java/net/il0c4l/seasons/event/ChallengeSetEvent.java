package net.il0c4l.seasons.event;

import net.il0c4l.seasons.storage.Entry;
import org.bukkit.command.CommandSender;

public class ChallengeSetEvent extends ChallengeEvent {

    private String command;
    private Entry entry;
    private int progress;
    private CommandSender sender;

    public ChallengeSetEvent(String command, Entry entry, int progress){
        super(entry);
        this.command = command;
        this.entry = entry;
        this.progress = progress;
        sender = null;
    }

    public ChallengeSetEvent(String command, Entry entry, int progress, CommandSender sender){
        super(entry);
        this.command = command;
        this.entry = entry;
        this.sender = sender;
        this.progress = progress;
    }

    public boolean setFromCommand(){
        return sender != null;
    }

    public CommandSender getSender(){
        return sender;
    }

    public int getProgress(){
        return progress;
    }

    public String getCommand(){
        return command;
    }
}
