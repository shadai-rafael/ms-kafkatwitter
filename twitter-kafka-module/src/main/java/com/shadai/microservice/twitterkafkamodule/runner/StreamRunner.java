package com.shadai.microservice.twitterkafkamodule.runner;

import twitter4j.TwitterException;

public interface StreamRunner {
    public void start() throws TwitterException;    
}
