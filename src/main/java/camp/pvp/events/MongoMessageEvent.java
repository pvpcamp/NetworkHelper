package camp.pvp.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;
import java.util.UUID;

public class MongoMessageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public MongoMessageEvent(String message, UUID uniqueId, Date requestStarted, Date requestFinished) {
        this.message = message;
        this.uniqueId = uniqueId;
        this.requestStarted = requestStarted;
        this.requestFinished = requestFinished;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
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
    private final UUID uniqueId;
    private final Date requestStarted, requestFinished;


}
