package camp.pvp.redis;

import camp.pvp.NetworkHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashSet;
import java.util.Set;

public class RedisSubscriber {

    private NetworkHelper plugin;
    private Jedis jedis;
    private JedisPubSub jedisPubSub;
    private @Getter Set<RedisSubscriberListener> listeners;
    private String channel;

    public RedisSubscriber(NetworkHelper plugin) {
        this.plugin = plugin;
        this.listeners = new HashSet<>();

        FileConfiguration config = plugin.getConfig();

        this.channel = config.getString("redis.channel");

        this.jedis = new Jedis(config.getString("redis.host"), config.getInt("redis.port"));

        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String c, String message) {
                if(c.equals(channel)) {
                    for(RedisSubscriberListener listener : listeners) {
                        RedisMessage m = new RedisMessage(message);
                        if(m.getInternalChannel().equals(listener.getChannel())) {
                            listener.onReceive(m.getElements());
                        }
                    }
                }
            }
        };

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> jedis.subscribe(jedisPubSub, channel));
    }
}
