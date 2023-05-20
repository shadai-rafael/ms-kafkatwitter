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

import com.shadai.microservice.twitterkafkamodule.runner.StreamRunner;
import com.shadai.microservice.twitterkafkamodule.config.TwitterKafkaModuleConfigData;
import com.shadai.microservice.twitterkafkamodule.listener.TwitterKafkaStatusListener;
import java.util.Arrays;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.FilterQuery;


@Component
@ConditionalOnProperty(name="twitter-kafka-module.enabled-mock-tweets", havingValue="false", matchIfMissing=true)
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
