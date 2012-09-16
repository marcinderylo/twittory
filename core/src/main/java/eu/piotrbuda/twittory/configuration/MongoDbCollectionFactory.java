package eu.piotrbuda.twittory.configuration;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * .
 */
public class MongoDbCollectionFactory {
    private DB db;

    public MongoDbCollectionFactory(MongoDbFactory dbFactory) {
        this.db = dbFactory.getDb();
    }

    public DBCollection getAccessTokensCollection() {
        return db.getCollection("accessTokens");
    }
}
