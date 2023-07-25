package camp.pvp.redis;

import com.google.gson.JsonObject;
import lombok.Getter;

public class RedisMessage {

    private @Getter String channel;
    private @Getter JsonObject message;

    protected RedisMessage(String channel, JsonObject message) {
        this.channel = channel;
        this.message = message;
    }
}
