package eu.piotrbuda.twittory.storage;

import com.mongodb.DBCollection;
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
    }

    public boolean validateAccessToken(String accessTokenKey) {
        return false;
    }
}
