package camp.pvp.listeners;

import camp.pvp.NetworkHelper;
import camp.pvp.events.MongoMessageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MongoMessageListener implements Listener {

    private NetworkHelper plugin;
    public MongoMessageListener(NetworkHelper plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMongoMessage(MongoMessageEvent event) {
            event.getSender().sendMessage(event.getMessage(true));
    }
}
