package com.shadai.twitter.kafka.runner.impl;

import com.shadai.twitter.kafka.config.TwitterKafkaConfiguration;
import com.shadai.twitter.kafka.listener.TwitterKafkaStatusListener;
import com.shadai.twitter.kafka.runner.StreamRunner;

import jakarta.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.FilterQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false")
public class KafkaStreamRunner implements StreamRunner {

    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private final TwitterKafkaConfiguration twitterKafkaConfiguration;
    private TwitterStream twitterStream;

    public KafkaStreamRunner(TwitterKafkaStatusListener twitterKafkaStatusListener,
            TwitterKafkaConfiguration twitterKafkaConfiguration){
                this.twitterKafkaConfiguration = twitterKafkaConfiguration;
                this.twitterKafkaStatusListener = twitterKafkaStatusListener;

    }

    @Override
    public void start() throws TwitterException{
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);
        addFilter();
    }

    @PreDestroy
    public void shutdown(){
        if(twitterStream != null){
            log.info("TwitterStream shutdown");      
            twitterStream.shutdown();
        }
    }

    private void addFilter(){
        String[] keywords = twitterKafkaConfiguration.getTwitterkeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
    }

}
