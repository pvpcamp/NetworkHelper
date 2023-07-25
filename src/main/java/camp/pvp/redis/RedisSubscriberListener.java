package camp.pvp.redis;

import com.google.gson.JsonObject;
import lombok.Getter;

public interface RedisSubscriberListener {
    void onReceive(JsonObject json);
}
