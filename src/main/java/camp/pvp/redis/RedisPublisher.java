package camp.pvp.redis;

import camp.pvp.NetworkHelper;
import com.google.gson.JsonObject;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.Queue;

public class RedisPublisher {

    private NetworkHelper plugin;
    private Jedis jedis;
    private Queue<RedisMessage> messageQueue;
    private final String channel;
    public RedisPublisher(NetworkHelper plugin) {
        this.plugin = plugin;
        this.messageQueue = new LinkedList<>();

        FileConfiguration config = plugin.getConfig();
        this.channel = config.getString("redis.channel");

        this.jedis = new Jedis(config.getString("redis.host"), config.getInt("redis.port"));

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if(!messageQueue.isEmpty()) {
                RedisMessage message = messageQueue.poll();
                jedis.publish(channel, message.getMessage().toString());
            }
        }, 0, 1);
    }

    public void publishMessage(String channel, JsonObject elements) {
        RedisMessage message = new RedisMessage(channel, elements);
        this.messageQueue.add(message);
    }

    public void publishMessage(RedisMessage message) {
        this.messageQueue.add(message);
    }

}
