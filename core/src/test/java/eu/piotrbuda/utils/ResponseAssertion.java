package eu.piotrbuda.utils;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * .
 */
public class ResponseAssertion {
    public static ResponseAssertion assertThatResponse(Response response) {
        return new ResponseAssertion(response);
    }

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

    public void containsCookie(String cookieName, String cookieValue) {
        assertTrue(response.getMetadata().containsKey("Set-Cookie"));
        List<Object> cookies = response.getMetadata().get("Set-Cookie");
        NewCookie cookie = (NewCookie) cookies.get(0);
        assertEquals(cookieName, cookie.getName());
        assertEquals(cookieValue, cookie.getValue());
    }
}
