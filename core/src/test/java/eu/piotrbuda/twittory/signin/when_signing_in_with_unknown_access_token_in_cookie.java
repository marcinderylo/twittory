package eu.piotrbuda.twittory.signin;

import eu.piotrbuda.twittory.storage.AccessTokenStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static eu.piotrbuda.utils.ResponseAssertion.assertThatResponse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * .
 */
@RunWith(MockitoJUnitRunner.class)
public class when_signing_in_with_unknown_access_token_in_cookie {
    @InjectMocks
    @SuppressWarnings("unused")
    private TwitterSignInResource resource;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AccessTokenStorage storage;

    @Test
    public void then_user_is_redirected_to_twitter_for_authentication() {
        givenACookieWithAccessToken();
        andUnknownAccessTokenSecret();
        userIsRedirectedToTwitterForAuthentication();
    }

    private void givenACookieWithAccessToken() {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("accesstoken", "token")});
    }

    private void andUnknownAccessTokenSecret() {
        when(storage.getAccessTokenDetails(anyString())).thenReturn(null);
    }

    private void userIsRedirectedToTwitterForAuthentication() {
        Response response = resource.signIn(request);
        accessTokenDetailsWereRetrieved();
        assertThatResponse(response).isRedirect();
        assertThatResponse(response).redirectsTo("http://api.twitter.com/oauth/authenticate?oauth_token=");
    }

    private void accessTokenDetailsWereRetrieved() {
        verify(storage).getAccessTokenDetails(anyString());
    }
}
