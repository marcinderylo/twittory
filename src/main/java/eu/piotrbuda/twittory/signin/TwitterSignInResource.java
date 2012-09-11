package eu.piotrbuda.twittory.signin;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

/**
 * .
 */
@Path("/twitter")
public class TwitterSignInResource {

    @GET
    @Path("signin")
    public void signIn(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(System.getenv("twitter4j.oauth.consumerKey"), System.getenv("twitter4j.oauth.consumerSecret"));
        String requestUrl = request.getRequestURL().toString();
        String requestUri = request.getRequestURI();
        String server = requestUrl.substring(0, requestUrl.indexOf(requestUri));
        String callback = server + "/api/twitter/callback";
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken(callback);
            request.getSession(true).setAttribute("requestToken", requestToken);
            response.sendRedirect(requestToken.getAuthenticationURL());
        } catch (TwitterException e) {
            throw new WebApplicationException(e);
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("callback")
    public Response twitterCallback(@QueryParam("oauth_token") String oauth_token,
                                    @QueryParam("oauth_verifier") String oauth_verifier) {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(System.getenv("twitter4j.oauth.consumerKey"), System.getenv("twitter4j.oauth.consumerSecret"));
        return Response.seeOther(URI.create("../twittory.html")).build();
    }
}
