package eu.piotrbuda.twittory.core;

import eu.piotrbuda.twittory.core.model.LinkDetails;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TweetLinkScannerTest {
    @Mock
    private Twitter twitter;
    @InjectMocks
    private TweetLinkScanner scanner;

    @Test
    public void returns_initialized_empty_list_no_links() throws Exception {
        givenTimelineContains(noTweets());
        thenScanForLinksShouldProduce(emptyList());
    }

    @Test
    public void returns_details_only_for_tweets_with_links() throws Exception {
        givenTimelineContains(aTweetWithNoLinks(), tweet("Text with link http://www.onet.pl in it"), aTweetWithNoLinks());
        thenScanForLinksShouldProduce(nResults(1));
    }

    @Test
    @Ignore("ooops, this one is not yet implemented!")
    public void returns_multiple_links_from_single_tweet() throws Exception {
        givenTimelineContains(tweet("this tweet has two links: http://www.onet.pl and http://www.redtube.com ;)"));
        thenScanForLinksShouldProduce(nResults(2));
    }

    private void givenTimelineContains(Status... tweets) throws TwitterException {
        ResponseList<Status> response = mock(ResponseList.class);
        when(response.iterator()).thenReturn(asList(tweets).iterator());
        when(twitter.getHomeTimeline()).thenReturn(response);
    }

    private void thenScanForLinksShouldProduce(List<LinkDetails> expectedList) {
        List<LinkDetails> scan = scanner.scan();
        assertEquals(expectedList, scan);
    }

    private void thenScanForLinksShouldProduce(Matcher<List<LinkDetails>> expectedListMatcher) {
        List<LinkDetails> scan = scanner.scan();
        assertThat(scan, expectedListMatcher);
    }

    private Status aTweetWithNoLinks() {
        return tweet("Status " + Math.random());
    }

    private Status[] noTweets() {
        return new Status[0];
    }


    private Status tweet(String text) {
        Status status = mock(Status.class);
        when(status.getText()).thenReturn(text);
        return status;
    }

    private List<LinkDetails> emptyList() {
        return Collections.emptyList();
    }

    private Matcher<List<LinkDetails>> nResults(final int expectedNumberOfResults) {
        return new TypeSafeMatcher<List<LinkDetails>>() {
            @Override
            public boolean matchesSafely(List<LinkDetails> item) {
                return item.size() == expectedNumberOfResults;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("list with ");
                description.appendValue(expectedNumberOfResults);
                description.appendText(" elements");
            }
        };
    }
}
