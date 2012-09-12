package eu.piotrbuda.twittory.signin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;

import static org.mockito.Mockito.*;

/**
 * Unit tests of {@link TwitterSignInResource}
 */
public class TwitterSignInResourceTest {

    private TwitterSignInResource resource;

    private Twitter twitter;

    private String oauth_token = "token";

    private String oauth_verifier = "verifier";

    private String oauth_secret = "secret";

    private HttpServletRequest request = mock(HttpServletRequest.class);

    private HttpSession session = mock(HttpSession.class);

    @Before
    public void setUp() throws Exception {
        resource = new TwitterSignInResource();
        twitter = mock(Twitter.class);
        resource.setTwitter(twitter);
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
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
        when(twitter.getOAuthAccessToken(any(RequestToken.class), anyString())).thenReturn(null);

        resource.callback(request, oauth_token, oauth_verifier);

        verify(twitter).getOAuthAccessToken(any(RequestToken.class), anyString());
    }
}
