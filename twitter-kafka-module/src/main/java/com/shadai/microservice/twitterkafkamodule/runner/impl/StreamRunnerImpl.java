package com.shadai.microservice.twitterkafkamodule.runnerimpl;

import com.shadai.microservice.twitterkafkamodule.runner.StreamRunner;
import com.shadai.microservice.twitterkafkamodule.config.TwitterKafkaModuleConfigData;
import com.shadai.microservice.twitterkafkamodule.listener.TwitterKafkaStatusListener;
import java.util.Arrays;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.FilterQuery;


@Component
public class StreamRunnerImpl implements StreamRunner{
   
    private static final Logger LOG = LoggerFactory.getLogger(StreamRunnerImpl.class);
    private final TwitterKafkaModuleConfigData configData;
    private final TwitterKafkaStatusListener listener;
    private TwitterStream stream;

    public StreamRunnerImpl(TwitterKafkaModuleConfigData configData,
                         TwitterKafkaStatusListener listener){

        this.configData = configData;
        this.listener = listener;
    }

    @PreDestroy
    public void shutDown(){
        if(stream != null){
            stream.shutdown();
        }
    }

    @Override
    public void start() throws TwitterException{
        stream = new TwitterStreamFactory().getInstance();
        stream.addListener(listener);
        String[] keywords = configData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filter = new FilterQuery(keywords);
        stream.filter(filter);
        LOG.info("Starting with kewords: "+ Arrays.toString(keywords));
    }
}
