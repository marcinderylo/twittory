package eu.piotrbuda.twittory.signin;

import eu.piotrbuda.twittory.storage.AccessTokenStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.auth.AccessToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * .
 */
@RunWith(MockitoJUnitRunner.class)
public class when_signing_in_with_known_access_token_in_cookie {
    @InjectMocks
    private TwitterSignInResource resource;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AccessTokenStorage storage;

    @Test
    public void then_user_is_redirected_to_twittory_main_page() {
        givenACookieWithAccessToken();
        andKnownAccessTokenSecret();
        userIsRedirectedToTwittory();
    }

    private void givenACookieWithAccessToken() {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("accesstoken", "token")});
    }

    private void andKnownAccessTokenSecret() {
        when(storage.getAccessTokenDetails(anyString())).thenReturn(new AccessToken("1-a", "secret"));
    }

    private void userIsRedirectedToTwittory() {
        Response response = resource.signIn(request);
        accessTokenDetailsWereRetrieved();
        assertThatResponse(response).isRedirect();
        assertThatResponse(response).redirectsTo("twittory.html");
    }

    private void accessTokenDetailsWereRetrieved() {
        verify(storage).getAccessTokenDetails(anyString());
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
