package com.devopsbuddy.exceptions;

/**
 * Created by tedonema on 13/05/2016.
 */
public class StripeException extends RuntimeException {


    public StripeException() {
        super();
    }

    public StripeException(Throwable e) {
        super(e);
    }
}
