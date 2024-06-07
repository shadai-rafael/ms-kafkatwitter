package com.shadai.twitter.kafka.listener;

import twitter4j.Status;
import twitter4j.StatusAdapter;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TwitterKafkaStatusListener extends StatusAdapter{

    @Override
    public void onStatus(Status status){
        log.info("Twitter status with text {}", status.getText());
    }

}
