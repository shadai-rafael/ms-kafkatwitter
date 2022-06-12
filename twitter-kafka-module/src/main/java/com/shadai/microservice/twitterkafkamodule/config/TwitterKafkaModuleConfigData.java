package com.shadai.microservice.twitterkafkamodule.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-kafka-module")
public class TwitterKafkaModuleConfigData {
    private List<String> twitterKeywords;
}
