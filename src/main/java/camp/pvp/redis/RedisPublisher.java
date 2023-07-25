package camp.pvp.redis;

import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.Queue;

public class RedisPublisher {

    private JavaPlugin plugin;
    private Jedis jedis;
    private Queue<RedisMessage> messageQueue;
    public RedisPublisher(JavaPlugin plugin, String host, int port) {
        this.plugin = plugin;
        this.messageQueue = new LinkedList<>();

        this.jedis = new Jedis(host, port);

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if(!messageQueue.isEmpty()) {
                RedisMessage message = messageQueue.poll();
                jedis.publish(message.getChannel(), message.getMessage().toString());
            }
        }, 0, 1);
    }

    public void publishMessage(String channel, JsonObject elements) {
        RedisMessage message = new RedisMessage(channel, elements);
        this.messageQueue.add(message);
    }
}
