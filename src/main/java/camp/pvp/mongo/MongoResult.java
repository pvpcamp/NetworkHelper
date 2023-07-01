package camp.pvp.mongo;

import org.bson.Document;

public interface MongoResult {
    void call(Document document);
}
