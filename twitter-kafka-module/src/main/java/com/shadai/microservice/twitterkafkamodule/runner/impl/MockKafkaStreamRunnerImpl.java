/*
MIT License
Copyright (c) 2023 shadai-rafael
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.shadai.microservice.twitterkafkamodule.runner.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.shadai.microservice.twitterkafkamodule.config.TwitterKafkaModuleConfigData;
import com.shadai.microservice.twitterkafkamodule.exception.TwitterToKafkaExeption;
import com.shadai.microservice.twitterkafkamodule.listener.TwitterKafkaStatusListener;
import com.shadai.microservice.twitterkafkamodule.runner.StreamRunner;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

@Component
@ConditionalOnProperty(name="twitter-kafka-module.enabled-mock-tweets", havingValue="true")
public class MockKafkaStreamRunnerImpl implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunnerImpl.class);

    private final TwitterKafkaModuleConfigData twitterKafkaModuleConfigData;

    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private static final Random RANDOM = new Random();

    private static final String[] WORDS = {
        "Lorem",
        "ipsum",
        "dolor",
        "sit",
        "amet,",
        "consectetur",
        "adipiscing", 
        "elit", 
        "sed",
        "do",
        "eiusmod",
        "tempor",
        "incididunt",
        "ut",
        "labore",
        "et",
        "dolore",
        "magna",
        "aliqua.",
        "Ut,",
        "enim",
        "ad",
        "minim",
        "veniam,",
        "quis",
        "nostrud",
        "exercitation",
        "ullamco",
        "laboris",
        "nisi",
        "ut",
        "aliquip",
        "ex",
        "ea",
        "commodo"
    };

    private static final String tweetAsRawJson = "{"+
                "\"created_at\":\"{0}\","+
                "\"id\":\"{1}\","+
                "\"text\":\"{2}\","+
                "\"user\":{\"id\":\"{3}\"}"+
                "}";

    public MockKafkaStreamRunnerImpl(TwitterKafkaModuleConfigData twitterKafkaModuleConfigData,
                    TwitterKafkaStatusListener twitterKafkaStatusListener){
        this.twitterKafkaModuleConfigData = twitterKafkaModuleConfigData;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    @Override
    public void start() throws TwitterException{
        String[] keywords = twitterKafkaModuleConfigData.getTwitterKeywords().toArray(new String[0]);
        Boolean enable = twitterKafkaModuleConfigData.getEnableMockTweets();
        Long mockSleepMs = twitterKafkaModuleConfigData.getMockSleepMs();
        Integer mockMinTweetLenght = twitterKafkaModuleConfigData.getMockMinTweetLenght();
        Integer mockMaxTweetLenght = twitterKafkaModuleConfigData.getMockMaxTweetLenght();
        LOG.info("Strating mock filtering twitter streams for keywords {}", Arrays.toString(keywords));
        twitterStreamSimulator(keywords, mockMinTweetLenght, mockMaxTweetLenght, mockSleepMs);

    }

    private void twitterStreamSimulator(String[] keywords, Integer mockMinTweetLenght, Integer mockMaxTweetLenght, Long mockSleepMs){
        Executors.newSingleThreadExecutor().submit((Runnable) ()->{
            try {
                while(true){
                    String formattedTwitAsRawJson = getFormattedTwit(keywords, mockMinTweetLenght, mockMaxTweetLenght);
                    Status status;
                    status = TwitterObjectFactory.createStatus(formattedTwitAsRawJson);
                    twitterKafkaStatusListener.onStatus(status);
                    sleep(mockSleepMs);
                }
                
            } catch ( TwitterToKafkaExeption | TwitterException e) {
                LOG.error("Error creating twitter status!", e);
            }
        }
        );
    }

    private void sleep (Long ms){
        try{
            Thread.sleep(ms);
        }catch(InterruptedException ex){
            throw new TwitterToKafkaExeption("Error while sleeping");
        }
        
    }

    private String getFormattedTwit(String[] keywords, int min, int max){
        String[] params = new String[]{
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
            String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
            getRandomtwitContent(keywords, min, max),
            String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };

        String twit = tweetAsRawJson;

        for(int i = 0;i < params.length ;i++){
            twit = twit.replace("{"+i+"}", params[i]);
        }
        return twit;
    }

    private String getRandomtwitContent(String[] keywords, int min, int max){
        StringBuilder twit = new StringBuilder();
        int twitLength = RANDOM.nextInt(max-min +1) + min;
        for(int i = 0; i < twitLength ; i++ ){
            twit.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if(i == twitLength/2){
                twit.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }
        }

        return twit.toString().trim();
    }
    
}
