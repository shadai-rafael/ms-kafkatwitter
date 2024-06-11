package com.shadai.twitter.listener.runner.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.shadai.twitter.configapp.listener.TwitterKafkaConfiguration;
import com.shadai.twitter.listener.exception.TwitterKafkaServiceException;
import com.shadai.twitter.listener.listener.TwitterKafkaStatusListener;
import com.shadai.twitter.listener.runner.StreamRunner;

import lombok.extern.slf4j.Slf4j;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

@Slf4j
@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true", matchIfMissing = true)
public class MockKafkaStreamRunner implements StreamRunner{

    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private final TwitterKafkaConfiguration twitterKafkaConfiguration;
    private static final Random RANDOM = new Random();
    private static final String[] WORDS = new String[]{
        "Lorem",
        "ipsum",
        "dolor",
        "sit",
        "amet",
        "consectetur",
        "adipiscing",
        "elit",
        "Nam",
        "pellentesque",
        "et",
        "metus",
        "ullamcorper",
        "pulvinar",
        "Cras", 
        "bibendum",
        "vel",
        "lectus",
        "vel",
        "mattis",
        "Sed",
        "ut",
        "odio",
        "nisi",
        "Praesent",
        "fermentum",
        "dui",
        "sed",
        "congue",
        "rhoncus",
        "magna",
        "feugiat",
        "orci",
        "vel",
        "vehicula",
        "velit",
        "urna",
        "vitae",
        "augue",
        "Vestibulum",
        "libero",
        "nibh",
        "accumsan",
        "ac",
        "felis",
        "id",
        "tempor"
    };

    private static final String tweetAsRawJson = 
        "{\"created_at\":\"{0}\"," +
        "\"id\":\"{1}\"," +
        "\"text\":\" {2} \", " +
        "\"user\":{\"id\":\"{3}\"}}";

    private static final String TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public MockKafkaStreamRunner(TwitterKafkaStatusListener twitterKafkaStatusListener,
            TwitterKafkaConfiguration twitterKafkaConfiguration){

                this.twitterKafkaConfiguration = twitterKafkaConfiguration;
                this.twitterKafkaStatusListener = twitterKafkaStatusListener;

    }

    @Override
    public void start() throws TwitterException {
        String[] keywords = twitterKafkaConfiguration.getTwitterkeywords().toArray(new String[0]);
        log.info("size of the array {}",twitterKafkaConfiguration.getTwitterkeywords().size() );
        int maxLenght = twitterKafkaConfiguration.getMockMaxTweetLenght();
        int minLenght = twitterKafkaConfiguration.getMockMinTweetLenght();
        long mockSleepMs = twitterKafkaConfiguration.getMockSleepMs();
        simulateTwitterStream(maxLenght, minLenght, keywords, mockSleepMs);
    }

    private void simulateTwitterStream(int maxLenght, int minLenght, String[] keywords, long mockSleepMs){
        log.info("Start mock twitter filtering");
        Executors.newSingleThreadExecutor().submit(()->{
            try {
                while (true) {
                    String formattedStringAsJson = getFormattedTweet(maxLenght, minLenght, keywords);
                    Status status = TwitterObjectFactory.createStatus(formattedStringAsJson);
                    twitterKafkaStatusListener.onStatus(status);
                    sleep(mockSleepMs);
                }                
            } catch (TwitterException e) {
                log.error("Message error: {}", e.getMessage());
            }
        });        

    }

    private String getFormattedTweet(int maxLenght, int minLenght, String[] keywords){
        String[] params = new String[]{
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_DATE_FORMAT)),
            String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
            getRandomTweetContent(maxLenght, minLenght, keywords),
            String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };

        String tweet = tweetAsRawJson;

        for(int i=0 ; i < params.length ; i++){
            tweet = tweet.replace("{" + i + "}", params[i]);
        }

        return tweet;
    }

    private String getRandomTweetContent(int maxLenght, int minLenght, String[] keywords){
        StringBuilder tweetBuilder = new StringBuilder();
        int tweetLenght = RANDOM.nextInt(maxLenght - minLenght + 1) + minLenght;
        for(int i =0; i < tweetLenght; i++){
            tweetBuilder.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if (i == tweetLenght/2) {
                tweetBuilder.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");                
            }
        }
        log.info("{}",tweetBuilder.toString());
        return tweetBuilder.toString().trim();        
    }

    private void sleep(Long sleepMs){
        try{
            Thread.sleep(sleepMs);
        }catch(InterruptedException ex){
            throw new TwitterKafkaServiceException("Error while sleeping for waiting new status to create!! ");
        }
    }

}
