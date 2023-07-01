package camp.pvp;

import camp.pvp.mongo.MongoManager;
import camp.pvp.redis.RedisPublisher;
import camp.pvp.redis.RedisSubscriber;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class NetworkHelper extends JavaPlugin {

    public @Getter static NetworkHelper instance;

    public @Getter MongoManager mongoManager;
    public @Getter RedisPublisher redisPublisher;
    public @Getter RedisSubscriber redisSubscriber;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        Logger logger = this.getLogger();

        boolean mongoEnabled = config.getBoolean("mongo.enabled");
        boolean redisEnabled = config.getBoolean("redis.enabled");

        if(mongoEnabled) {
            mongoManager = new MongoManager(this);
            logger.info("MongoManager has been loaded and successfully connected");
        } else {
            logger.info("MongoDB has been disabled in config, skipping MongoDB connection and setup.");
        }

        if(redisEnabled) {
            redisPublisher = new RedisPublisher(this);
            redisSubscriber = new RedisSubscriber(this);
            logger.info("RedisPublisher and RedisSubscriber have both been loaded and successfully connected.");
        } else {
            logger.info("Redis has been disabled in config, skipping Redis connection and setup.");
        }

        if(!mongoEnabled && !redisEnabled) {
            logger.warning("MongoDB and Redis are both disabled, could this be a first time setup?" +
                    "If you have plugins that depend on " + this.getName() + ", please fix this immediately.");
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
