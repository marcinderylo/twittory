package eu.piotrbuda.twittory;

import eu.piotrbuda.twittory.core.TweetLinkScanner;
import eu.piotrbuda.twittory.core.model.LinkDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.List;

/**
 * .
 */
@Component
@Path("/twittory")
public class TwittoryResource {

    public List<LinkDetails> getLatestLinks() {
        List<LinkDetails> details = scanner.scan();
        return details;
    }

    private TweetLinkScanner scanner;

    @Autowired
    public void setScanner(TweetLinkScanner scanner) {
        this.scanner = scanner;
    }
}
