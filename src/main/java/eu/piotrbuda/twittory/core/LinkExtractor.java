package eu.piotrbuda.twittory.core;

import twitter4j.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * .
 */
public class LinkExtractor {
    private static final Pattern SCHEMA_PATTERN = Pattern.compile("https?://[-\\w+&@#/%=~()|?!:,.;]*[-\\w+&@#/%=~()|]", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCHEMALESS_PATTERN = Pattern.compile("www\\.[-\\w+&@#/%=~()|?!:,.;]*[-\\w+&@#/%=~()|]", Pattern.CASE_INSENSITIVE);

    public String extractLinkFrom(Status status) {
        checkNotNull(status, "Status cannot be null");
        String text = status.getText();
        String url = matchPattern(SCHEMA_PATTERN, text);
        if(url.equals("")) {
            url = matchPattern(SCHEMALESS_PATTERN, text);
            if(!url.equals("")) {
                url = "http://" + url;
            }
            return url;
        }
        return url;
    }

    private String matchPattern(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return text.substring(matcher.start(), matcher.end());
        }
        return "";
    }
}
