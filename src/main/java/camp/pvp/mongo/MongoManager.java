package camp.pvp.mongo;

import camp.pvp.NetworkHelper;
import camp.pvp.utils.ThreadUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class MongoManager {

    private NetworkHelper plugin;
    private Logger logger;
    private MongoClient client;
    private MongoDatabase database;

    public MongoManager(NetworkHelper plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();

        FileConfiguration config = plugin.getConfig();

        MongoClientSettings mcs = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(new ConnectionString(config.getString("mongo.uri")))
                .build();
        client = MongoClients.create(mcs);
        database = client.getDatabase(config.getString("mongo.db"));
    }

    public void createDocument(boolean async, String collectionName, Object id) {
        ThreadUtil.runTask(async, this.plugin, () -> {
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document document = new Document("_id", id);

            collection.insertOne(document);
        });
    }

    public void getDocument(boolean async, String collectionName, Object id, MongoResult result) {
        ThreadUtil.runTask(async, plugin, () -> {
            MongoCollection<Document> collection = database.getCollection(collectionName);

            if (collection.find(Filters.eq("_id", id)).iterator().hasNext()) {
                result.call(collection.find(Filters.eq("_id", id)).first());
            } else {
                result.call(null);
            }
        });
    }

    public void massUpdate(final boolean async, final MongoUpdate mongoUpdate) {
        massUpdate(async, mongoUpdate.getCollectionName(), mongoUpdate.getId(), mongoUpdate.getUpdate());
    }

    public void massUpdate(final boolean async, final String collectionName, final Object id, final Map<String, Object> updates) {
        ThreadUtil.runTask(async, plugin, () -> {
            final MongoCollection<Document> collection = database.getCollection(collectionName);

            Document document = collection.find(new Document("_id", id)).first();
            if(document == null) {
                collection.insertOne(new Document("_id", id));
            }

            updates.forEach((key, value) -> collection.updateOne(Filters.eq("_id", id), Updates.set(key, value)));
        });
    }

    public void createCollection(boolean async, String collectionName) {
        ThreadUtil.runTask(async, plugin, () -> {
            AtomicBoolean exists = new AtomicBoolean(false);
            database.listCollectionNames().forEach(s -> {
                if(s.equals(collectionName)) {
                    exists.set(true);
                }
            });

            if(!exists.get()) {
                database.createCollection(collectionName);
            }
        });
    }

    public void getCollection(boolean async, String collectionName, MongoCollectionResult mcr) {
        ThreadUtil.runTask(async, plugin, () -> mcr.call(database.getCollection(collectionName)));
    }

    public void getCollectionIterable(boolean async, String collectionName, MongoIterableResult mir) {
        ThreadUtil.runTask(async, plugin, ()-> mir.call(database.getCollection(collectionName).find()));
    }
}
