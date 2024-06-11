package com.shadai.twitter.listener;

import com.shadai.twitter.configapp.listener.TwitterKafkaConfiguration;
import com.shadai.twitter.listener.runner.StreamRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;

import java.util.Arrays;


@Slf4j
@SpringBootApplication
@AllArgsConstructor
@ComponentScan(basePackages = "com.shadai.twitter")
public class KafkaTwitterApplication implements CommandLineRunner{

	private final TwitterKafkaConfiguration kafkaTwitterConfiguration;
	private final StreamRunner streamRunner;

	public static void main(String[] args) {
		SpringApplication.run(KafkaTwitterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		log.info(Arrays.toString(kafkaTwitterConfiguration.getTwitterkeywords().toArray(new String[] {})));
		log.info(kafkaTwitterConfiguration.getWelcomemessage());
		streamRunner.start();
	}

}
