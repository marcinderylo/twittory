package eu.piotrbuda.twittory.storage;

import com.mongodb.DBCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link AccessTokenStorage}
 */
@RunWith(MockitoJUnitRunner.class)
public class AccessTokenStorageTest {
    @Mock
    private DBCollection accessTokensCollection;

    @InjectMocks
    private AccessTokenStorage storage;

    @Test(expected = NullPointerException.class)
    public void cant_store_null_access_token() throws Exception {
        storage.storeAccessToken(null);
    }
}
