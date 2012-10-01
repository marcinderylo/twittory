package eu.piotrbuda.twittory.core;

import org.junit.Test;
import twitter4j.Status;

import static java.lang.String.format;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkExtractorTest {
    private LinkExtractor extractor = new LinkExtractor(); // that's JUnit - it behaves like a good citizen so can do it

    @Test(expected = NullPointerException.class)
    public void throws_exception_when_status_is_null() throws Exception {
        extractor.extractLinkFrom(null);// oh come on, do we really need to be THAT defensive? who'd pass a null in there?
        // I guess twitter timeline doesn't have null tweets...
    }

    @Test
    public void returns_empty_string_for_status_without_links() throws Exception {
        assertThatTweet("There is no link in status").
                hasNoLinks();
    }

    @Test
    public void returns_link_that_begins_with_http() throws Exception {
        assertThatTweet("There a link in status: http://www.onet.pl").
                hasLink("http://www.onet.pl");
    }

    @Test
    public void returns_link_that_begins_with_http_with_text_after_link() throws Exception {
        assertThatTweet("There a link in status: http://www.onet.pl in the middle of the text").
                hasLink("http://www.onet.pl");
    }

    @Test
    public void returns_link_that_begins_with_www() throws Exception {
        assertThatTweet("There a link in status: www.onet.pl").
                hasLink("http://www.onet.pl");
    }


    private LinkExtractorAssertion assertThatTweet(String text) {
        Status tweet = mock(Status.class);
        when(tweet.getText()).thenReturn(text);
        return new LinkExtractorAssertion(tweet);
    }

    private class LinkExtractorAssertion {
        private Status tweet;

        public LinkExtractorAssertion(Status tweet) {
            this.tweet = tweet;
        }

        public void hasLink(String expectedLink) {
            String foundLink = extractor.extractLinkFrom(tweet);
            assertEquals(format("expected link in tweet: \"%s\"", tweet.getText()), expectedLink, foundLink);
        }

        public void hasNoLinks() {
            String foundLink = extractor.extractLinkFrom(tweet);
            assertTrue(format("expected no links in tweet: \"%s\"", tweet.getText()), "".equals(foundLink));
        }
    }
}
