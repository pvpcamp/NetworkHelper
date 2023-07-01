package camp.pvp.utils;

import org.bukkit.plugin.java.JavaPlugin;

public class ThreadUtil {

    public static void runTask(boolean async, JavaPlugin plugin, Runnable runnable) {
        if(async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }
}
