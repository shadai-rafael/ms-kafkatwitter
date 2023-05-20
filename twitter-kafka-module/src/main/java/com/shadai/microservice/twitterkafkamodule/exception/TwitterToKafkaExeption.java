package com.shadai.microservice.twitterkafkamodule.exception;

public class TwitterToKafkaExeption extends RuntimeException {

    public TwitterToKafkaExeption (){
        super();
    }

    public TwitterToKafkaExeption(String message){
        super(message);
    }
    
    public TwitterToKafkaExeption(String message, Throwable cause){
        super(message, cause);
    }
}
