package eu.piotrbuda.twittory.signin;

import eu.piotrbuda.twittory.storage.AccessTokenStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * .
 */
@RunWith(MockitoJUnitRunner.class)
public class when_access_token_is_already_available {

    @InjectMocks
    private TwitterSignInResource resource;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Twitter twitter;

    @Mock
    private AccessTokenStorage storage;

    @Mock
    private HttpSession session = mock(HttpSession.class);

    @Before
    public void setUp() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[0]);
        when(request.getRequestURL()).thenReturn(new StringBuffer("uri"));
        when(request.getRequestURI()).thenReturn("uri");
        when(request.getSession(true)).thenReturn(session);
    }

    @Test
    public void should_redirect_to_twittory() throws Exception {
        whenAccessTokenIsAlreadyAvailable();
        userIsRedirectedToTwittory();
    }

    private void whenAccessTokenIsAlreadyAvailable() throws TwitterException {
        when(twitter.getOAuthRequestToken(anyString())).thenThrow(IllegalStateException.class);
    }

    private void userIsRedirectedToTwittory() {
        Response response = resource.signIn(request);
        assertThatResponse(response).isRedirect();
        assertThatResponse(response).redirectsTo("twittory.html");
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
