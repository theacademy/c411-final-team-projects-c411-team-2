package org.buildATrip.service;

public class AmadeusServiceException extends Exception {
    public AmadeusServiceException(String s) {
        super(s);
    }
    public AmadeusServiceException(String s, Throwable cause) {
        super(s,cause);
    }
}
