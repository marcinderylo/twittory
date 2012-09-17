package eu.piotrbuda.twittory.storage;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.lang3.Validate;
import twitter4j.auth.AccessToken;

/**
 * Storage for access tokens.
 */
public class AccessTokenStorage {

    private DBCollection accessTokens;

    public AccessTokenStorage(DBCollection accessTokens) {
        this.accessTokens = accessTokens;
    }

    public void storeAccessToken(AccessToken accessToken) {
        Validate.notNull(accessToken, "Access token cannot be null");
        DBObject object = BasicDBObjectBuilder.start()
                .add("_id", accessToken.getUserId())
                .add("accessTokenKey", accessToken.getToken())
                .add("accessTokenSecret", accessToken.getTokenSecret())
                .get();
        accessTokens.save(object);
    }

    public AccessToken getAccessTokenDetails(String accessTokenKey) {
        Validate.notBlank(accessTokenKey, "Please provide access token key");
        DBObject query = BasicDBObjectBuilder.start()
                .add("accessTokenKey", accessTokenKey)
                .get();
        DBCursor cursor = accessTokens.find(query);
        if (cursor.size() == 1) {
            DBObject retrievedAccessToken = cursor.next();
            return new AccessToken(
                    (String) retrievedAccessToken.get("accessTokenKey"),
                    (String) retrievedAccessToken.get("accessTokenSecret")
            );
        }
        return null;
    }

}
