package eu.piotrbuda.twittory.signin;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;

import static org.junit.Assert.*;

/**
 * .
 */
public class TwitterSignInResourceIntegrationTest extends JerseyTest {
    public TwitterSignInResourceIntegrationTest() {
        super(new WebAppDescriptor.Builder("eu.piotrbuda.twittory.signin")
                .contextParam("contextConfigLocation", "classpath:applicationContext_test.xml")
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .build());
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
