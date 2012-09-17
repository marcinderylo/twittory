package eu.piotrbuda.twittory.storage;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.auth.AccessToken;

import static org.junit.Assert.*;

/**
 * .
 */
public class AccessTokenStorageIntegration_getAccessTokenDetails_Test {
    private AccessTokenStorage storage;
    private DBCollection accessTokensCollection;
    private DB testDatabase;

    @Before
    public void setUp() throws Exception {
        Mongo mongo = new Mongo();
        testDatabase = mongo.getDB("test_twittory");
        accessTokensCollection = testDatabase.getCollection("accessTokens");
        storage = new AccessTokenStorage(accessTokensCollection);
    }

    @After
    public void tearDown() throws Exception {
        testDatabase.dropDatabase();
        storage = null;
        testDatabase = null;
        accessTokensCollection = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void cant_query_with_blank_access_token_key() throws Exception {
        storage.getAccessTokenDetails("");
    }

    @Test
    public void asking_for_access_token_key_that_is_not_stored_returns_null() throws Exception {
        assertNull(storage.getAccessTokenDetails("invalidTokenKey"));
    }

    @Test
    public void asking_for_a_stored_access_token_returns_it() throws Exception {
        String token = "1-token";
        String tokenSecret = "secret";
        givenAStoredAccessToken(token, tokenSecret);
        AccessToken accessToken = storage.getAccessTokenDetails(token);
        assertNotNull("Retrieval of existing token should return it", accessToken);
        assertEquals(token, accessToken.getToken());
        assertEquals(tokenSecret, accessToken.getTokenSecret());
    }

    private void givenAStoredAccessToken(String token, String tokenSecret) {
        storage.storeAccessToken(new AccessToken(token, tokenSecret));
    }
}
