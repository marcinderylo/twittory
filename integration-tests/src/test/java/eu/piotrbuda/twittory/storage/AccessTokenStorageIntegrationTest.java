package eu.piotrbuda.twittory.storage;

import com.mongodb.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.auth.AccessToken;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests for {@link AccessTokenStorage}
 */
public class AccessTokenStorageIntegrationTest {

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

    @Test(expected = NullPointerException.class)
    public void cant_store_null_access_token() throws Exception {
        storage.storeAccessToken(null);
    }

    @Test
    public void access_token_is_stored() throws Exception {
        AccessToken token = new AccessToken("12345-token", "token-secret");
        storage.storeAccessToken(token);

        DBCursor actual = accessTokensCollection.find();
        assertEquals(1, actual.count());

        DBObject storedToken = actual.next();
        assertEquals(12345L, storedToken.get("_id"));
        assertEquals("12345-token", storedToken.get("accessTokenKey"));
        assertEquals("token-secret", storedToken.get("accessTokenSecret"));
    }

    @Test
    public void saving_multiple_times_the_same_access_token_updates_it() throws Exception {
        AccessToken token1 = new AccessToken("12345-token", "token-secret");
        AccessToken token2 = new AccessToken("12345-token", "another-secret");

        //store the first token
        storage.storeAccessToken(token1);
        //now store the modified token but for the same user
        storage.storeAccessToken(token2);

        DBCursor actual = accessTokensCollection.find();
        assertEquals(1, actual.count());

        DBObject storedToken = actual.next();
        assertEquals(12345L, storedToken.get("_id"));
        assertEquals("12345-token", storedToken.get("accessTokenKey"));
        assertEquals("another-secret", storedToken.get("accessTokenSecret"));
    }
}
