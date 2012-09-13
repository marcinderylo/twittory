package eu.piotrbuda.twittory.core;

import eu.piotrbuda.twittory.core.model.LinkDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * .
 */
public class TweetLinkScannerTest {
    private TweetLinkScanner scanner;

    private Twitter twitter;

    @Before
    public void setUp() throws Exception {
        twitter = mock(Twitter.class);
        scanner = new TweetLinkScanner(twitter);
    }

    @After
    public void tearDown() throws Exception {
        scanner = null;
    }

    @Test
    public void returns_initialized_empty_list_no_links() throws Exception {
        ResponseList<Status> mockResponse = mock(ResponseList.class);
        when(mockResponse.iterator()).thenReturn(Collections.EMPTY_LIST.iterator());
        when(twitter.getHomeTimeline()).thenReturn(mockResponse);
        List<LinkDetails> scan = scanner.scan();
        assertNotNull(scan);
        assertEquals(0, scan.size());
    }

    @Test
    public void returns_details_only_for_tweets_with_links() throws Exception {
        Status status1 = prepareStatus("Status 1");
        Status status2 = prepareStatus("Text with link http://www.onet.pl in it");
        Status status3 = prepareStatus("Status 3");
        ResponseList<Status> mockResponse = mock(ResponseList.class);

        when(mockResponse.iterator()).thenReturn(Arrays.asList(status1, status2, status3).iterator());
        when(twitter.getHomeTimeline()).thenReturn(mockResponse);

        List<LinkDetails> scan = scanner.scan();
        assertNotNull(scan);
        assertEquals(1, scan.size());
    }

    private Status prepareStatus(String text) {
        Status status = mock(Status.class);
        when(status.getText()).thenReturn(text);
        return status;
    }
}
