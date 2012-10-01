package eu.piotrbuda.twittory.signin;

import eu.piotrbuda.twittory.storage.AccessTokenStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static eu.piotrbuda.utils.ResponseAssertion.assertThatResponse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Unit tests of {@link TwitterSignInResource}
 */
@RunWith(MockitoJUnitRunner.class)
public class TwitterSignInResourceTest {

    public static final String OAUTH_TOKEN = "token";
    public static final String OAUTH_VERIFIER = "verifier";
    public static final String OAUTH_SECRET = "secret";
    @InjectMocks
    private TwitterSignInResource resource;

    @Mock
    private Twitter twitter;

    @Mock
    private AccessTokenStorage storage;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        when(request.getSession()).thenReturn(session); // mocks returning mocks... no good...
    }

    @After
    public void tearDown() throws Exception {
        resource = null;
    }

    @Test(expected = WebApplicationException.class)
    public void it_is_impossible_to_call_callback_without_request_token() throws Exception {
        // given
        withoutRequestTokenInSession();
        // when
        resource.callback(request, "", "");
        // then
        expectExceptionToBeThrown();
    }

    @Test
    public void access_token_is_stored_upon_retrieval() throws Exception {
        // given
        withRequestTokenInSession(new RequestToken(OAUTH_TOKEN, OAUTH_SECRET));
        whenTwitterReturnsAccessToken(new AccessToken("1-token", "secret"));
        // when
        resource.callback(request, OAUTH_TOKEN, OAUTH_VERIFIER);
        // then
        verify(storage).storeAccessToken(new AccessToken("1-token", "secret"));
    }

    @Test
    public void access_token_key_is_stored_in_cookie() throws Exception {
        // given
        String token = "1-token";
        withRequestTokenInSession(new RequestToken(OAUTH_TOKEN, OAUTH_SECRET));
        whenTwitterReturnsAccessToken(new AccessToken(token, "secret"));
        // when
        Response response = resource.callback(request, OAUTH_TOKEN, OAUTH_VERIFIER);
        // then
        assertThatResponse(response).containsCookie("accesstoken", token);
    }

    private void withoutRequestTokenInSession() {
        withRequestTokenInSession(null);
    }

    private void withRequestTokenInSession(RequestToken requestToken) {
        when(session.getAttribute("requestToken")).thenReturn(requestToken);
    }

    private void whenTwitterReturnsAccessToken(AccessToken secret) throws TwitterException {
        when(twitter.getOAuthAccessToken(any(RequestToken.class), anyString())).thenReturn(secret);
    }

    private void expectExceptionToBeThrown() {
        fail("exception should have been thrown");
    }
}
