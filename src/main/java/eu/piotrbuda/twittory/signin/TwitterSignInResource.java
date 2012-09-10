package eu.piotrbuda.twittory.signin;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * .
 */
@Path("/twitter")
public class TwitterSignInResource {

    @GET
    @Path("signin")
    public Response signIn(@Context HttpServletRequest request) {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(System.getenv("twitter4j.oauth.consumerKey"), System.getenv("twitter4j.oauth.consumerSecret"));
        String callback = "http://localhost:8080/twitter/callback";
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken(callback);
            request.getSession(true).setAttribute("requestToken", requestToken);
            return Response.seeOther(URI.create(requestToken.getAuthenticationURL())).build();
        } catch (TwitterException e) {
            throw new WebApplicationException(e);
        }
    }
}
