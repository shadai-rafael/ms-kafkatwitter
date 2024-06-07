package com.shadai.twitter.listener.runner;

import twitter4j.TwitterException;

public interface StreamRunner {

    public void start() throws TwitterException;

}
