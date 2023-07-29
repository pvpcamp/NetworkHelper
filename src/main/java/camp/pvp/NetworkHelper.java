package camp.pvp;

import camp.pvp.command.CommandHandler;
import camp.pvp.listeners.MongoMessageListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class NetworkHelper extends JavaPlugin {

    @Getter private static NetworkHelper instance;
    @Getter CommandHandler commandHandler;

    @Override
    public void onEnable() {
        instance = this;
        new MongoMessageListener(this);
        commandHandler = new CommandHandler(this);
    }
}