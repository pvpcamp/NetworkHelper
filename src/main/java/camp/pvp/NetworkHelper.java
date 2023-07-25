package camp.pvp;

import camp.pvp.listeners.MongoMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NetworkHelper extends JavaPlugin {

    @Override
    public void onEnable() {
        new MongoMessageListener(this);
    }
}
