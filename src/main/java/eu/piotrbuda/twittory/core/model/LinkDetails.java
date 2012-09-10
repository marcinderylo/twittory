package eu.piotrbuda.twittory.core.model;

/**
 * .
 */
public class LinkDetails {
    private String userName;

    private Long tweetId;

    private String url;

    public LinkDetails() {
    }

    public LinkDetails(String userName, Long tweetId, String url) {
        this.userName = userName;
        this.tweetId = tweetId;
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
