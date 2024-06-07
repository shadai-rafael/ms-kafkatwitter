package com.shadai.twitter.kafka.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * TwitterToKafkaConfiguration
 */
@Data
@Configuration
@ConfigurationProperties(prefix="twitter-to-kafka-service")
public class TwitterKafkaConfiguration {
    private List<String>  twitterkeywords;
    private String welcomemessage;
    private Boolean enableMockTweets;
    private Long mockSleepMs;
    private Integer mockMaxTweetLenght;
    private Integer mockMinTweetLenght;
}