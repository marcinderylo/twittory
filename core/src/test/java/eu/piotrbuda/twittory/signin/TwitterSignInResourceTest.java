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
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests of {@link TwitterSignInResource}
 */
@RunWith(MockitoJUnitRunner.class)
public class TwitterSignInResourceTest {

    @InjectMocks
    private TwitterSignInResource resource;

    @Mock
    private Twitter twitter;

    @Mock
    private AccessTokenStorage storage;

    private String oauth_token = "token";

    private String oauth_verifier = "verifier";

    private String oauth_secret = "secret";

    @Mock
    private HttpServletRequest request = mock(HttpServletRequest.class);

    @Mock
    private HttpSession session = mock(HttpSession.class);

    @Before
    public void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
    }

    @After
    public void tearDown() throws Exception {
        resource = null;
    }

    @Test(expected = WebApplicationException.class)
    public void it_is_impossible_to_call_callback_without_request_token() throws Exception {
        when(session.getAttribute("requestToken")).thenReturn(null);

        resource.callback(request, "", "");
    }

    @Test
    public void token_is_obtained() throws Exception {
        when(session.getAttribute("requestToken")).thenReturn(new RequestToken(oauth_token, oauth_secret));

        resource.callback(request, oauth_token, oauth_verifier);

        verify(twitter).getOAuthAccessToken(any(RequestToken.class), anyString());
    }

    @Test
    public void access_token_is_stored_upon_retrieval() throws Exception {
        AccessToken accessToken = new AccessToken("1-token", "secret");
        when(session.getAttribute("requestToken")).thenReturn(new RequestToken(oauth_token, oauth_secret));
        when(twitter.getOAuthAccessToken(any(RequestToken.class), anyString())).thenReturn(accessToken);

        resource.callback(request, oauth_token, oauth_verifier);

        verify(storage).storeAccessToken(accessToken);
    }

    @Test
    public void when_access_token_is_already_available_should_redirect_to_twittory() throws Exception {
        whenAccessTokenIsAlreadyAvailable();
        Response response = resource.signIn(request);
        assertThatResponse(response).isRedirect();
        assertThatResponse(response).redirectsTo("twittory.html");
    }

    private void whenAccessTokenIsAlreadyAvailable() throws TwitterException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("uri"));
        when(request.getRequestURI()).thenReturn("uri");
        when(request.getSession(true)).thenReturn(session);
        //getOAuthRequestToken throws IllegalStateException when access token is already available
        when(twitter.getOAuthRequestToken(anyString())).thenThrow(IllegalStateException.class);
    }

    private ResponseAssertion assertThatResponse(Response response) {
        return new ResponseAssertion(response);
    }

    private class ResponseAssertion {
        private Response response;

        public ResponseAssertion(Response response) {
            this.response = response;
        }

        public void isRedirect() {
            assertEquals(Response.Status.SEE_OTHER.getStatusCode(), response.getStatus());
        }

        public void redirectsTo(String url) {
            assertTrue(response.getMetadata().containsKey("Location"));
            List<Object> location = response.getMetadata().get("Location");
            assertTrue(location.get(0).toString().contains(url));
        }
    }
}
