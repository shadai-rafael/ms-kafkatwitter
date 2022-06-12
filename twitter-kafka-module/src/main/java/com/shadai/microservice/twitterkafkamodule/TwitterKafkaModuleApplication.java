package com.shadai.microservice.twitterkafkamodule;

import com.shadai.microservice.twitterkafkamodule.config.TwitterKafkaModuleConfigData;
import com.shadai.microservice.twitterkafkamodule.runner.StreamRunner;
import java.util.Arrays;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;


@SpringBootApplication
public class TwitterKafkaModuleApplication implements CommandLineRunner{

	private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaModuleApplication.class);

	private final TwitterKafkaModuleConfigData configData;

	private final StreamRunner runner;

	public TwitterKafkaModuleApplication(TwitterKafkaModuleConfigData configData,
											StreamRunner runner){
		this.configData = configData;
		this.runner = runner;
	}
    public static void main(String[] args) {
		SpringApplication.run(TwitterKafkaModuleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("App starts...");
        LOG.info(Arrays.toString(configData.getTwitterKeywords().toArray(new String[] {})));
		runner.start();
	}
}
