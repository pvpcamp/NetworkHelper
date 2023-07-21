package camp.pvp.redis;

import com.google.gson.JsonObject;
import lombok.Getter;

public abstract class RedisSubscriberListener {

    private final @Getter String channel;
    public RedisSubscriberListener(String channel) {
        this.channel = channel;
    }

    public abstract void onReceive(JsonObject json);
}
