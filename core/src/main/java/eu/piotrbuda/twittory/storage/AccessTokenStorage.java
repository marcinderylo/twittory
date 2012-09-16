package eu.piotrbuda.twittory.storage;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import twitter4j.auth.AccessToken;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Storage for access tokens.
 */
public class AccessTokenStorage {

    private DBCollection accessTokens;

    public AccessTokenStorage(DBCollection accessTokens) {
        this.accessTokens = accessTokens;
    }

    public void storeAccessToken(AccessToken accessToken) {
        checkNotNull(accessToken, "Access token cannot be null");
        DBObject object = BasicDBObjectBuilder.start()
                .add("_id", accessToken.getUserId())
                .add("accessTokenKey", accessToken.getToken())
                .add("accessTokenSecret", accessToken.getTokenSecret())
                .get();
        accessTokens.save(object);
    }

}
