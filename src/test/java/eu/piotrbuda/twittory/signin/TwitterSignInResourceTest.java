package eu.piotrbuda.twittory.signin;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * .
 */
public class TwitterSignInResourceTest extends JerseyTest {
    public TwitterSignInResourceTest() {
        super("eu.piotrbuda.twittory");
    }

    @Test
    public void signin_should_redirect_to_twitter() throws Exception {
        ClientResponse clientResponse = resource().path("/twitter/signin").get(ClientResponse.class);
        assertNotNull(clientResponse);
        assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());
        String expectedTwitterUrl = "http://api.twitter.com/oauth/authenticate?oauth_token=";
        assertTrue(clientResponse.toString().contains(expectedTwitterUrl));
    }
}
