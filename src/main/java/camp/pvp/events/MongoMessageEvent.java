package camp.pvp.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;
import java.util.UUID;

public class MongoMessageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public MongoMessageEvent(String message, CommandSender sender, Date requestStarted) {
        this.message = message;
        this.sender = sender;
        this.requestStarted = requestStarted;
        this.requestFinished = new Date();
    }

    public String getMessage(boolean includeTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        if(includeTime) {
            sb.append(" &7(");
            sb.append(requestFinished.getTime() - requestStarted.getTime());
            sb.append(" ms)");
        }

        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private final String message;
    @Getter private final CommandSender sender;
    private final Date requestStarted, requestFinished;


}
