package com.shadai.microservice.twitterkafkamodule.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.StatusAdapter;
import twitter4j.Status;


@Component
public class TwitterKafkaStatusListener extends StatusAdapter{
    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

    @Override
    public void onStatus(Status status){
        LOG.info("Status : "+status.getText());
    }
}
