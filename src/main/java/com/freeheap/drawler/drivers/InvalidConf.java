package com.freeheap.drawler.drivers;

/**
 * Created by william on 7/11/16.
 */
public class InvalidConf extends Exception {

    public InvalidConf(String message) {
        super(message);
    }

    public InvalidConf() {
    }

    public InvalidConf(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConf(Throwable cause) {
        super(cause);
    }

    public InvalidConf(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
