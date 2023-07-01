package camp.pvp.mongo;

import com.mongodb.client.FindIterable;
import org.bson.Document;

public interface MongoIterableResult {
    void call(FindIterable<Document> iterable);
}
