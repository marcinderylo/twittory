Building
========

Twittory requires You to have a valid consumer key and secret for Twitter. To obtain these
you need to go to https://dev.twitter.com and register an application. Only then you will be granted
consumer key and secret which then needs to be passed to Twitter4J that is used under the hood.

Currently in order for Twittory to work You have to set two environment variables:

* twitter4j.oauth.consumerKey
* twitter4j.oauth.consumerSecret