package com.devopsbuddy.exceptions;

/**
 * Created by tedonema on 14/05/2016.
 */
public class S3Exception extends RuntimeException {

    public S3Exception(Throwable e) {
        super(e);
    }

    public S3Exception(String errorMessage) {
        super(errorMessage);
    }
}
