package eu.piotrbuda.twittory.core;

import eu.piotrbuda.twittory.core.model.LinkDetails;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 */
public class TweetLinkScanner {
    private Twitter twitter;

    public TweetLinkScanner(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<LinkDetails> scan() {
        List<LinkDetails> list = new ArrayList<LinkDetails>();
        try {
            ResponseList<Status> homeTimeline = twitter.getHomeTimeline();
            LinkExtractor extractor = new LinkExtractor();
            for (Status status : homeTimeline) {
                String url = extractor.extractLinkFrom(status);
                if(!url.equals("")) {
                    LinkDetails details = new LinkDetails(twitter.getScreenName(), status.getId(), url);
                    list.add(details);
                }
            }
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
