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
