package com.shadai.twitter.listener.exception;

public class TwitterKafkaServiceException extends RuntimeException {

    public TwitterKafkaServiceException(){
        super();
    }

    public TwitterKafkaServiceException(String message){
        super(message);
    }

    public TwitterKafkaServiceException(String message, Throwable cause){
        super(message, cause);
    }
    
}