package eu.piotrbuda.twittory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 * .
 */
public class TwitterConfiguringBean {
    public Twitter configure(TwitterFactory factory) {
        Twitter twitter = factory.getInstance();
        String key = System.getenv("twitter4j.oauth.consumerKey");
        String secret = System.getenv("twitter4j.oauth.consumerSecret");
        twitter.setOAuthConsumer(key, secret);
        return twitter;
    }
}
