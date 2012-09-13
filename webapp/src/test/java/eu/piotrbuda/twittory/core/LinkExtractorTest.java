package eu.piotrbuda.twittory.core;

import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * .
 */
public class LinkExtractorTest {
    private LinkExtractor extractor;
    private Status status;

    @Before
    public void setUp() throws Exception {
        extractor = new LinkExtractor();
        status = mock(Status.class);
    }

    @Test(expected = NullPointerException.class)
    public void throws_exception_when_status_is_null() throws Exception {
        extractor.extractLinkFrom(null);
    }

    @Test
    public void returns_empty_string_for_status_without_links() throws Exception {
        given(status.getText()).willReturn("There is no link in status");
        String link = extractor.extractLinkFrom(status);
        assertEquals("", link);
    }

    @Test
    public void returns_link_that_begins_with_http() throws Exception {
        given(status.getText()).willReturn("There a link in status: http://www.onet.pl");
        String link = extractor.extractLinkFrom(status);
        assertEquals("http://www.onet.pl", link);
    }

    @Test
    public void returns_link_that_begins_with_http_with_text_after_link() throws Exception {
        given(status.getText()).willReturn("There a link in status: http://www.onet.pl in the middle of the text");
        String link = extractor.extractLinkFrom(status);
        assertEquals("http://www.onet.pl", link);
    }

    @Test
    public void returns_link_that_begins_with_www() throws Exception {
        given(status.getText()).willReturn("There a link in status: www.onet.pl");
        String link = extractor.extractLinkFrom(status);
        assertEquals("http://www.onet.pl", link);
    }
}
