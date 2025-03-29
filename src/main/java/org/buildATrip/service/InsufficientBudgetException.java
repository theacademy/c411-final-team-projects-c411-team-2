package org.buildATrip.service;

public class InsufficientBudgetException extends Exception {

    public InsufficientBudgetException() {
    }

    public InsufficientBudgetException(String message) {
        super(message);
    }
}
