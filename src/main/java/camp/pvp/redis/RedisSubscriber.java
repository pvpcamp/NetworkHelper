package camp.pvp.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber {

    private JavaPlugin plugin;
    private Jedis jedis;
    private JedisPubSub jedisPubSub;
    private RedisSubscriberListener listener;

    public RedisSubscriber(JavaPlugin plugin, String host, int port, String channel, RedisSubscriberListener listener) {
        this.plugin = plugin;
        this.jedis = new Jedis(host, port);
        this.listener = listener;

        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String c, String message) {
                JsonParser jp = new JsonParser();
                JsonObject json = jp.parse(message).getAsJsonObject();
                RedisSubscriber.this.listener.onReceive(json);
            }
        };

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> jedis.subscribe(jedisPubSub, channel));
    }
}
